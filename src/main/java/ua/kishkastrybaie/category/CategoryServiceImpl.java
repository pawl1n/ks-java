package ua.kishkastrybaie.category;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;
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
  public CategoryDto create(CategoryRequestDto categoryRequestDto) {
    Category category = categoryMapper.toDomain(categoryRequestDto);
    return categoryModelAssembler.toModel(categoryRepository.save(category));
  }

  @Override
  public CategoryDto replace(Long id, CategoryRequestDto categoryRequestDto) {
    Category categoryDetails = categoryMapper.toDomain(categoryRequestDto);

    Category category =
        categoryRepository
            .findById(id)
            .map(
                p -> {
                  p.setName(categoryDetails.getName());
                  p.setParentCategory(categoryDetails.getParentCategory());
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

  @Override
  public CategoryDto getParentCategory(Long id) {
    Category category =
        categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    Category parentCategory =
        Optional.ofNullable(category.getParentCategory())
            .orElseThrow(() -> new CategoryNotFoundException("No parent category for id: " + id));

    return categoryModelAssembler.toModel(parentCategory);
  }
}
