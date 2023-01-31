package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.CategoryDTO;
import ua.kishkastrybaie.controller.dto.CategoryMapper;
import ua.kishkastrybaie.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.findAll().stream().map(categoryMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable Long id) {
        return categoryMapper.toDto(categoryService.findById(id));
    }

    @PostMapping
    public void save(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryMapper.toDomain(categoryDTO));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.update(id, categoryMapper.toDomain(categoryDTO));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
