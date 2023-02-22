package ua.kishkastrybaie.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.exception.CategoryNotFoundException;
import ua.kishkastrybaie.repository.CategoryRepository;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.service.CategoryService;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category categoryDetails) {
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

    @Override
    public Category replace(Long id, Category newCategory) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(newCategory.getName());
            return categoryRepository.save(category);
        }).orElseGet(() -> {
            newCategory.setId(id);
            return categoryRepository.save(newCategory);
        });
    }

    @Override
    public void deleteById(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category createIfNotExists(String name) {
        Supplier<Category> createCategory = () -> {
            Category category = new Category();
            category.setName(name);

            return categoryRepository.save(category);
        };

        return categoryRepository.findCategoryByName(name).orElseGet(createCategory);
    }

    @Override
    public Category getParentCategory(Long id) {
        Category category = findById(id).getParentCategory();
        if (category == null) throw new CategoryNotFoundException();

        return category;
    }
}
