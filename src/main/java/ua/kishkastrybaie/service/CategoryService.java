package ua.kishkastrybaie.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.repository.CategoryRepository;
import ua.kishkastrybaie.repository.entity.Category;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Category categoryDetails) {
        Category category = categoryRepository.findById(categoryDetails.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (categoryDetails.getName() != null) {
            category.setName(categoryDetails.getName());
        }
        if (categoryDetails.getParentCategory() != null) {
            category.setParentCategory(categoryDetails.getParentCategory());
        }

        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category createIfNotExists(String name) {
        Supplier<Category> createCategory = () -> {
            Category category = new Category();
            category.setName(name);

            return save(category);
        };

        return categoryRepository.findCategoryByName(name).orElseGet(createCategory);
    }
}
