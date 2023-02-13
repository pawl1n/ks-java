package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.Category;

public interface CategoryService extends Service<Category, Long> {
    Category createIfNotExists(String name);
    Category getParentCategory(Long id);
}
