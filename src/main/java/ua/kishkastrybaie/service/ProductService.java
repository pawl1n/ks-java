package ua.kishkastrybaie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.repository.ProductRepository;
import ua.kishkastrybaie.repository.entity.Product;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void update(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException();
        }

        product.setId(id);
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
