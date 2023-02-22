package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.Category;

public interface CategoryService {
    Iterable<Category> findAll();
    Category findById(Long id);
    Category create(Category category);
    Category update(Long id, Category category);
    Category replace(Long id, Category category);
    void deleteById(Long id);
    Category createIfNotExists(String name);
    Category getParentCategory(Long id);
}
