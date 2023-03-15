package ua.kishkastrybaie.product;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.image.ImageDto;
import ua.kishkastrybaie.product.item.ProductItemDto;
import ua.kishkastrybaie.product.item.ProductItemRequestDto;

public interface ProductService {
  CollectionModel<ProductDto> findAll(Pageable pageable);

  ProductDto findById(Long id);

  ProductDto create(ProductRequestDto productRequestDto);

  ProductDto replace(Long id, ProductRequestDto productRequestDto);

  void deleteById(Long id);

  CategoryDto getProductCategory(Long id);

  ImageDto getProductImage(Long id);

  CollectionModel<ProductItemDto> getProductVariations(Long id);

  ProductItemDto getVariation(Long id, Long variationId);

  ProductItemDto addVariation(Long id, ProductItemRequestDto productItemRequestDto);

  ProductItemDto replaceVariation(Long id, Long variationId, ProductItemRequestDto productItemRequestDto);

  void deleteVariation(Long id, Long variationId);
}
