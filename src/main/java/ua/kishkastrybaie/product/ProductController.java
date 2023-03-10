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
