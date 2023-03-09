package ua.kishkastrybaie.service;

import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.CategoryRequestDto;

public interface CategoryService {
  CollectionModel<CategoryDto> findAll();

  CategoryDto findById(Long id);

  CategoryDto create(CategoryRequestDto category);

  CategoryDto replace(Long id, CategoryRequestDto category);

  void deleteById(Long id);

  CategoryDto getParentCategory(Long id);
}
