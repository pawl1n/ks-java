package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.ProductItem;

public interface ProductItemService {
    Iterable<ProductItem> findAll();
    ProductItem findById(Long id);
    ProductItem create(ProductItem entity);
    ProductItem update(Long id, ProductItem entity);
    ProductItem replace(Long id, ProductItem entity);
    void deleteById(Long id);
}
