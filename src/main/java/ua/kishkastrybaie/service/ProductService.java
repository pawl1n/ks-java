package ua.kishkastrybaie.service;

import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.ProductRequestDto;

public interface ProductService {
  CollectionModel<ProductDto> findAll();

  boolean existsById(Long id);

  ProductDto findById(Long id);

  ProductDto create(ProductRequestDto product);

  ProductDto replace(Long id, ProductRequestDto product);

  void deleteById(Long id);

  CategoryDto getProductCategory(Long id);
}
