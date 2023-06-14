package ua.kishkastrybaie.category;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

public interface CategoryService {
  CollectionModel<CategoryDto> findAll(Pageable pageable);

  CollectionModel<CategoryDto> findAllChildren(Long parentCategoryId);

  CategoryDto findById(Long id);

  CategoryDto create(CategoryRequestDto category);

  CategoryDto replace(Long id, CategoryRequestDto category);

  void deleteById(Long id);
}
