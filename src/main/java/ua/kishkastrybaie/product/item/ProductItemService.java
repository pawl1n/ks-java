package ua.kishkastrybaie.product.item;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

public interface ProductItemService {
  CollectionModel<ProductItemDto> findAllByProductId(Long productId, Pageable pageable);

  ProductItemDto findByProductIdAndId(Long productId, Long variationId);

  ProductItemDto create(Long productId, ProductItemRequestDto requestDto);

  ProductItemDto replace(Long productId, Long variationId, ProductItemRequestDto requestDto);

  void delete(Long productId, Long variationId);
}
