package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.ErrorDto;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.mapper.ProductMapper;
import ua.kishkastrybaie.exception.ProductNotFoundException;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.repository.entity.Product;
import ua.kishkastrybaie.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductService productService;
    private final RepresentationModelAssembler<Product, ProductDto> productModelAssembler;
    private final RepresentationModelAssembler<Category, CategoryDto> categoryModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<ProductDto>> all() {
        CollectionModel<ProductDto> responseDto = productModelAssembler.toCollectionModel(productService.findAll());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> one(@PathVariable Long id) {
        ProductDto responseDto = productModelAssembler.toModel(productService.findById(id));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/category")
    public ResponseEntity<CategoryDto> category(@PathVariable Long id) {
        CategoryDto responseDto = categoryModelAssembler.toModel(productService.findById(id).getCategory());

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        Product product = productService.create(productMapper.toDomain(productDto));
        ProductDto responseDto = productModelAssembler.toModel(product);

        return ResponseEntity
                .created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> replace(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = productService.update(id, productMapper.toDomain(productDto));
        ProductDto responseDto = productModelAssembler.toModel(product);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> handleProductNotFound(ProductNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
