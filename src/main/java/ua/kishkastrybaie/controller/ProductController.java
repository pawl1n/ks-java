package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.mapper.ProductMapper;
import ua.kishkastrybaie.repository.entity.Product;
import ua.kishkastrybaie.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        List<ProductDto> responseDto = productMapper.toDto(productService.findAll());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        ProductDto responseDto = productMapper.toDto(productService.findById(id));

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        Product response = productService.save(productMapper.toDomain(productDto));
        ProductDto responseDto = productMapper.toDto(response);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto) {
        Product response = productService.update(productMapper.toDomain(productDto));
        ProductDto responseDto = productMapper.toDto(response);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
