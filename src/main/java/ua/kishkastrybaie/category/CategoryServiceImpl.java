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

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final PagedResourcesAssembler<Category> pagedResourcesAssembler;
  private final CategoryTreeModelAssembler categoryTreeModelAssembler;

  @Override
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
  public CategoryDto findById(Long id) {
    return categoryModelAssembler.toModel(
        categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
  }

  @Override
  public CollectionModel<CategoryDto> findAllDescendants(Long parentCategoryId) {
    return categoryModelAssembler.toCollectionModel(
        categoryRepository.findAllDescendantsById(parentCategoryId));
  }

  @Override
  @Transactional
  public CategoryDto create(CategoryRequestDto categoryRequestDto) {
    Category category = new Category();
    category.setName(categoryRequestDto.name());

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
                p -> {
                  if (categoryRepository.isCategoryAncestorOfOther(
                      id, categoryRequestDto.parentCategory())) {
                    throw new CyclicCategoryPathException(id, categoryRequestDto.parentCategory());
                  }

                  p.setName(categoryRequestDto.name());
                  p.setParentCategory(parentCategory);
                  return categoryRepository.save(p);
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
}
