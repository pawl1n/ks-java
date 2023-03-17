package ua.kishkastrybaie.product.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{productId}/variations")
@RequiredArgsConstructor
@Slf4j
public class ProductItemController {
  private final ProductItemService productItemService;

  @GetMapping
  public ResponseEntity<CollectionModel<ProductItemDto>> all(@PathVariable Long productId, @PageableDefault Pageable pageable) {
    log.info("Get product variations by id: {}", productId);

    CollectionModel<ProductItemDto> responseDto = productItemService.findAllByProductId(productId, pageable);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{variationId}")
  public ResponseEntity<ProductItemDto> one(
      @PathVariable Long productId, @PathVariable Long variationId) {
    log.info("Get variation by id: {} and variation id: {}", productId, variationId);

    ProductItemDto responseDto = productItemService.findByProductIdAndId(productId, variationId);

    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<ProductItemDto> save(
      @PathVariable Long productId,
      @Valid @RequestBody ProductItemRequestDto productItemRequestDto) {
    log.info("Add variation to product by id: {}", productId);

    ProductItemDto responseDto = productItemService.create(productId, productItemRequestDto);

    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @DeleteMapping("/{variationId}")
  public ResponseEntity<Void> delete(@PathVariable Long productId, @PathVariable Long variationId) {
    log.info("Delete variation by id: {} and variation id: {}", productId, variationId);

    productItemService.delete(productId, variationId);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{variationId}")
  public ResponseEntity<ProductItemDto> replace(
      @PathVariable Long productId,
      @PathVariable Long variationId,
      @Valid @RequestBody ProductItemRequestDto productItemRequestDto) {
    log.info("Replace variation by id: {} and variation id: {}", productId, variationId);

    ProductItemDto responseDto =
        productItemService.replace(productId, variationId, productItemRequestDto);

    return ResponseEntity.ok(responseDto);
  }
}
