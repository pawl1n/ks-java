package ua.kishkastrybaie.product.item;

import org.springframework.hateoas.CollectionModel;

public interface ProductItemService {
    CollectionModel<ProductItemDto> getProductVariations(Long id);

    ProductItemDto getVariation(Long id, Long variationId);

    ProductItemDto addVariation(Long id, ProductItemRequestDto productItemRequestDto);

    ProductItemDto replaceVariation(Long id, Long variationId, ProductItemRequestDto productItemRequestDto);

    void deleteVariation(Long id, Long variationId);
}
