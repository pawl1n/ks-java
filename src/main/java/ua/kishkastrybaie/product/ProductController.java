package ua.kishkastrybaie.product;

import jakarta.validation.Valid;
import java.util.Optional;
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

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public ResponseEntity<CollectionModel<ProductDto>> all(
      @RequestParam Optional<String> categoryPath, @PageableDefault Pageable pageable) {

    CollectionModel<ProductDto> result =
        categoryPath
            .map(
                path -> {
                  log.info("Get products by category path: {}", path);
                  return productService.findByCategoryPath(path, pageable);
                })
            .orElseGet(
                () -> {
                  log.info("Get all products");
                  return productService.findAll(pageable);
                });

    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> one(@PathVariable Long id) {
    log.info("Get product by id: {}", id);

    ProductDto responseDto = productService.findById(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/slug/{slug}")
  public ResponseEntity<ProductDto> one(@PathVariable String slug) {
    log.info("Get product by slug: {}", slug);

    ProductDto responseDto = productService.findBySlug(slug);
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
}
