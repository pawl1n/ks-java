package ua.kishkastrybaie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.repository.CategoryRepository;
import ua.kishkastrybaie.repository.entity.Category;

import java.util.List;
import java.util.NoSuchElementException;

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

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void update(Long id, Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException();
        }

        category.setId(id);
        categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
