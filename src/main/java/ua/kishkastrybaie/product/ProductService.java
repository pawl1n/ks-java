package ua.kishkastrybaie.product;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.image.ImageDto;

public interface ProductService {
  CollectionModel<ProductDto> findAll(Pageable pageable);

  ProductDto findById(Long id);

  ProductDto findBySlug(String slug);

  CollectionModel<ProductDto> findByCategoryPath(String path, Pageable pageable);

  ProductDto create(ProductRequestDto productRequestDto);

  ProductDto replace(Long id, ProductRequestDto productRequestDto);

  void deleteById(Long id);

  CategoryDto getProductCategory(Long id);

  ImageDto getProductImage(Long id);
}
