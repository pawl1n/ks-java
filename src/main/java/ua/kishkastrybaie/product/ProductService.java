package ua.kishkastrybaie.product;

import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.category.CategoryDto;

public interface ProductService {
  CollectionModel<ProductDto> findAll();

  ProductDto findById(Long id);

  ProductDto create(ProductRequestDto productRequestDto);

  ProductDto replace(Long id, ProductRequestDto productRequestDto);

  void deleteById(Long id);

  CategoryDto getProductCategory(Long id);
}
