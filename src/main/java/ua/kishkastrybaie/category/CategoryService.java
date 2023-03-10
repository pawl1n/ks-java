package ua.kishkastrybaie.category;

import org.springframework.hateoas.CollectionModel;

public interface CategoryService {
  CollectionModel<CategoryDto> findAll();

  CategoryDto findById(Long id);

  CategoryDto create(CategoryRequestDto category);

  CategoryDto replace(Long id, CategoryRequestDto category);

  void deleteById(Long id);

  CategoryDto getParentCategory(Long id);
}
