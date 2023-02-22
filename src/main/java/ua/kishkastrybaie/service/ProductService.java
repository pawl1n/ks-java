package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.Product;

public interface ProductService {
    Iterable<Product> findAll();
    Product findById(Long id);
    Product create(Product product);
    Product update(Long id, Product product);
    Product replace(Long id, Product product);
    void deleteById(Long id);
}
