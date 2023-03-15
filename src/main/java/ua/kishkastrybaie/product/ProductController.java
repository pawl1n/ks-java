package ua.kishkastrybaie.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.image.ImageDto;
import ua.kishkastrybaie.product.item.ProductItemDto;
import ua.kishkastrybaie.product.item.ProductItemRequestDto;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public ResponseEntity<CollectionModel<ProductDto>> all(@PageableDefault Pageable pageable) {
    log.info("Get all products");

    CollectionModel<ProductDto> responseDto = productService.findAll(pageable);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> one(@PathVariable Long id) {
    log.info("Get product by id: {}", id);

    ProductDto responseDto = productService.findById(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/category")
  public ResponseEntity<CategoryDto> category(@PathVariable Long id) {
    log.info("Get product category by id: {}", id);

    CategoryDto responseDto = productService.getProductCategory(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/image")
  public ResponseEntity<ImageDto> image(@PathVariable Long id) {
    log.info("Get product image by id: {}", id);

    ImageDto responseDto = productService.getProductImage(id);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductRequestDto productRequestDto) {
    log.info("Save product: {}", productRequestDto);

    ProductDto responseDto = productService.create(productRequestDto);
    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> replace(
      @PathVariable Long id, @Valid @RequestBody ProductRequestDto productRequestDto) {
    log.info("Replace product by id: {}", id);

    ProductDto responseDto = productService.replace(id, productRequestDto);

    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("Delete product by id: {}", id);

    productService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/variations")
  public ResponseEntity<CollectionModel<ProductItemDto>> variations(@PathVariable Long id) {
    log.info("Get product variations by id: {}", id);

    CollectionModel<ProductItemDto> responseDto = productService.getProductVariations(id);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping("/{id}/variations")
  public ResponseEntity<ProductItemDto> addVariation(
      @PathVariable Long id, @Valid @RequestBody ProductItemRequestDto productItemRequestDto) {
    log.info("Add variation to product by id: {}", id);

    ProductItemDto responseDto = productService.addVariation(id, productItemRequestDto);

    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @GetMapping("/{id}/variations/{variationId}")
  public ResponseEntity<ProductItemDto> variation(
      @PathVariable Long id, @PathVariable Long variationId) {
    log.info("Get variation by id: {} and variation id: {}", id, variationId);

    ProductItemDto responseDto = productService.getVariation(id, variationId);

    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{id}/variations/{variationId}")
  public ResponseEntity<Void> deleteVariation(
      @PathVariable Long id, @PathVariable Long variationId) {
    log.info("Delete variation by id: {} and variation id: {}", id, variationId);

    productService.deleteVariation(id, variationId);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/variations/{variationId}")
  public ResponseEntity<ProductItemDto> replaceVariation(
      @PathVariable Long id,
      @PathVariable Long variationId,
      @Valid @RequestBody ProductItemRequestDto productItemRequestDto) {
    log.info("Replace variation by id: {} and variation id: {}", id, variationId);

    ProductItemDto responseDto =
        productService.replaceVariation(id, variationId, productItemRequestDto);

    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }
}
