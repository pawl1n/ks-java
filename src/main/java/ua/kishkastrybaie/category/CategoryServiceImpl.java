package ua.kishkastrybaie.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryModelAssembler categoryModelAssembler;
  private final PagedResourcesAssembler<Category> pagedResourcesAssembler;

  @Override
  public CollectionModel<CategoryDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(
        categoryRepository.findAll(pageable), categoryModelAssembler);
  }

  @Override
  public CategoryDto findById(Long id) {
    return categoryModelAssembler.toModel(
        categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
  }

  @Override
  public CollectionModel<CategoryDto> findAllChildren(Long parentCategoryId) {
    return categoryModelAssembler.toCollectionModel(
        categoryRepository.findAllDescendants(parentCategoryId));
  }

  @Override
  public CategoryDto create(CategoryRequestDto categoryRequestDto) {
    Category category = new Category();
    category.setName(categoryRequestDto.name());

    Category parentCategory = getCategory(categoryRequestDto.parentCategory());
    category.setParentCategory(parentCategory);

    return categoryModelAssembler.toModel(categoryRepository.save(category));
  }

  @Override
  public CategoryDto replace(Long id, CategoryRequestDto categoryRequestDto) {
    Category parentCategory = getCategory(categoryRequestDto.parentCategory());

    Category category =
        categoryRepository
            .findById(id)
            .map(
                p -> {
                  p.setName(categoryRequestDto.name());

                  p.setParentCategory(parentCategory);

                  return categoryRepository.save(p);
                })
            .orElseThrow(() -> new CategoryNotFoundException(id));

    return categoryModelAssembler.toModel(category);
  }

  @Override
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
