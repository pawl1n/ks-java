package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.CategoryRequestDto;
import ua.kishkastrybaie.service.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<CollectionModel<CategoryDto>> all() {
    log.info("Get all categories");

    CollectionModel<CategoryDto> responseDto = categoryService.findAll();
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> one(@PathVariable Long id) {
    log.info("Get category by id: {}", id);

    CategoryDto responseDto = categoryService.findById(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/parent-category")
  public ResponseEntity<CategoryDto> parentCategory(@PathVariable Long id) {
    log.info("Get parent category by id: {}", id);

    CategoryDto responseDto = categoryService.getParentCategory(id);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> save(@RequestBody CategoryRequestDto categoryRequestDto) {
    log.info("Save category: {}", categoryRequestDto);

    CategoryDto responseDto = categoryService.create(categoryRequestDto);
    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> update(
      @PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
    log.info("Update category by id: {}", id);

    CategoryDto responseDto = categoryService.replace(id, categoryRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("Delete category by id: {}", id);

    categoryService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
