package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.ErrorDto;
import ua.kishkastrybaie.controller.dto.mapper.CategoryMapper;
import ua.kishkastrybaie.exception.CategoryNotFoundException;
import ua.kishkastrybaie.repository.entity.Category;
import ua.kishkastrybaie.service.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    private final RepresentationModelAssembler<Category, CategoryDto> categoryModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<CategoryDto>> all() {
        CollectionModel<CategoryDto> responseDto = categoryModelAssembler.toCollectionModel(categoryService.findAll());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> one(@PathVariable Long id) {
        CategoryDto responseDto = categoryModelAssembler.toModel(categoryService.findById(id));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/parent-category")
    public ResponseEntity<CategoryDto> parentCategory(@PathVariable Long id) {
        CategoryDto responseDto = categoryModelAssembler.toModel(categoryService.getParentCategory(id));

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> save(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.create(categoryMapper.toDomain(categoryDto));
        CategoryDto responseDto = categoryModelAssembler.toModel(category);

        return ResponseEntity
                .created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.update(id, categoryMapper.toDomain(categoryDto));
        CategoryDto responseDto = categoryModelAssembler.toModel(category);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static ResponseEntity<ErrorDto> handleCategoryNotFound(CategoryNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
