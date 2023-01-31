package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.ProductDTO;
import ua.kishkastrybaie.controller.dto.ProductMapper;
import ua.kishkastrybaie.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.findAll().stream().map(productMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Long id) {
        return productMapper.toDTO(productService.findById(id));
    }

    @PostMapping
    public void save(@RequestBody ProductDTO productDTO) {
        productService.save(productMapper.toDomain(productDTO));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productService.update(id, productMapper.toDomain(productDTO));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
