package ua.kishkastrybaie.product.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aoi/products/{productId}/variations")
@RequiredArgsConstructor
@Slf4j
public class ProductItemController {
    private final ProductItemService productItemService;

    @GetMapping
    public ResponseEntity<CollectionModel<ProductItemDto>> variations(@PathVariable Long productId) {
        log.info("Get product variations by id: {}", productId);

        CollectionModel<ProductItemDto> responseDto = productItemService.getProductVariations(productId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ProductItemDto> addVariation(
            @PathVariable Long productId, @Valid @RequestBody ProductItemRequestDto productItemRequestDto) {
        log.info("Add variation to product by id: {}", productId);

        ProductItemDto responseDto = productItemService.addVariation(productId, productItemRequestDto);

        return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(responseDto);
    }

    @GetMapping("/{variationId}")
    public ResponseEntity<ProductItemDto> variation(
            @PathVariable Long productId, @PathVariable Long variationId) {
        log.info("Get variation by id: {} and variation id: {}", productId, variationId);

        ProductItemDto responseDto = productItemService.getVariation(productId, variationId);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{variationId}")
    public ResponseEntity<Void> deleteVariation(
            @PathVariable Long productId, @PathVariable Long variationId) {
        log.info("Delete variation by id: {} and variation id: {}", productId, variationId);

        productItemService.deleteVariation(productId, variationId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{variationId}")
    public ResponseEntity<ProductItemDto> replaceVariation(
            @PathVariable Long productId,
            @PathVariable Long variationId,
            @Valid @RequestBody ProductItemRequestDto productItemRequestDto) {
        log.info("Replace variation by id: {} and variation id: {}", productId, variationId);

        ProductItemDto responseDto =
                productItemService.replaceVariation(productId, variationId, productItemRequestDto);

        return ResponseEntity.ok(responseDto);
    }
}
