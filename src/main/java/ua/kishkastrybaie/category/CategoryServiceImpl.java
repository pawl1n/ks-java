package ua.kishkastrybaie.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.category.tree.CategoryTreeDto;
import ua.kishkastrybaie.category.tree.CategoryTreeModelAssembler;
import ua.kishkastrybaie.shared.SlugService;
import ua.kishkastrybaie.variation.VariationRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final PagedResourcesAssembler<Category> pagedResourcesAssembler;
  private final CategoryTreeModelAssembler categoryTreeModelAssembler;
  private final VariationRepository variationRepository;

  @Override
  @Transactional
  public CollectionModel<CategoryDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(
        categoryRepository.findAll(pageable), categoryModelAssembler);
  }

  @Override
  @Transactional
  public CollectionModel<CategoryDto> findRootCategories() {
    return categoryModelAssembler.toCollectionModel(categoryRepository.findRootCategories());
  }

  @Override
  @Transactional
  public CollectionModel<CategoryTreeDto> getTree() {
    List<Category> rootCategories = categoryRepository.findRootCategories();

    return categoryTreeModelAssembler.toCollectionModel(rootCategories);
  }

  @Override
  @Transactional
  public CategoryDto findById(Long id) {
    return categoryModelAssembler.toModel(
        categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
  }

  @Override
  @Transactional
  public CategoryTreeDto findByPath(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }

    String ltreePath = path.replace("/", ".").replace("-", "_");
    if (ltreePath.startsWith(".")) {
      ltreePath = ltreePath.substring(1);
    }

    return categoryTreeModelAssembler.toModel(
        categoryRepository
            .findByPath(ltreePath)
            .orElseThrow(
                () -> new CategoryNotFoundException("Category not found with path: " + path)));
  }

  @Override
  @Transactional
  public CollectionModel<CategoryDto> findAllDescendants(Long parentCategoryId) {
    return categoryModelAssembler.toCollectionModel(
        categoryRepository.findAllDescendantsById(parentCategoryId));
  }

  @Override
  @Transactional
  public CategoryDto create(CategoryRequestDto categoryRequestDto) {
    Category category = new Category();
    category.setName(categoryRequestDto.name());
    category.setSlug(generateSlug(categoryRequestDto));

    if (categoryRequestDto.variations() != null) {
      category.setVariations(variationRepository.findAllById(categoryRequestDto.variations()));
    }

    Category parentCategory = getCategory(categoryRequestDto.parentCategory());
    category.setParentCategory(parentCategory);

    return categoryModelAssembler.toModel(categoryRepository.save(category));
  }

  @Override
  @Transactional
  public CategoryDto replace(Long id, CategoryRequestDto categoryRequestDto) {
    Category parentCategory = getCategory(categoryRequestDto.parentCategory());

    Category category =
        categoryRepository
            .findById(id)
            .map(
                c -> {
                  if (categoryRepository.isCategoryAncestorOfOther(
                      id, categoryRequestDto.parentCategory())) {
                    throw new CyclicCategoryPathException(id, categoryRequestDto.parentCategory());
                  }

                  c.setName(categoryRequestDto.name());
                  c.setParentCategory(parentCategory);
                  c.setSlug(generateSlug(categoryRequestDto));
                  if (categoryRequestDto.variations() != null) {
                    c.setVariations(variationRepository.findAllById(categoryRequestDto.variations()));
                  }
                  return categoryRepository.save(c);
                })
            .orElseThrow(() -> new CategoryNotFoundException(id));

    return categoryModelAssembler.toModel(category);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Category category =
        categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    categoryRepository.delete(category);
  }

  private Category getCategory(Long id) {
    if (id != null) {
      if (!categoryRepository.existsById(id)) {
        throw new CategoryNotFoundException(id);
      }
      return categoryRepository.getReferenceById(id);
    }

    return null;
  }

  private String generateSlug(CategoryRequestDto requestDto) {
    if (requestDto.slug() != null && !requestDto.slug().isBlank()) {
      return SlugService.slugify(requestDto.slug());
    }

    return SlugService.slugify(requestDto.name());
  }
}
