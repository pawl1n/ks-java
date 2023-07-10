package ua.kishkastrybaie.category;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.category.tree.CategoryTreeDto;

public interface CategoryService {
  CollectionModel<CategoryDto> findAll(Pageable pageable);

  CollectionModel<CategoryDto> findRootCategories();

  CollectionModel<CategoryTreeDto> getTree();

  CollectionModel<CategoryDto> findAllDescendants(Long parentCategoryId);

  CategoryDto findById(Long id);

  CategoryTreeDto findByPath(String path);

  CategoryDto create(CategoryRequestDto category);

  CategoryDto replace(Long id, CategoryRequestDto category);

  void deleteById(Long id);
}
