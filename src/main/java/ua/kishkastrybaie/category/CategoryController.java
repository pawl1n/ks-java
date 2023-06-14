package ua.kishkastrybaie.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<CollectionModel<CategoryDto>> all(@PageableDefault Pageable pageable) {
    log.info("Get all categories");

    CollectionModel<CategoryDto> responseDto = categoryService.findAll(pageable);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> one(@PathVariable Long id) {
    log.info("Get category by id: {}", id);

    CategoryDto responseDto = categoryService.findById(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/children")
  public ResponseEntity<CollectionModel<CategoryDto>> children(@PathVariable Long id) {
    log.info("Get category children by id: {}", id);

    CollectionModel<CategoryDto> responseDto = categoryService.findAllChildren(id);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> save(
      @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    log.info("Save category: {}", categoryRequestDto);

    CategoryDto responseDto = categoryService.create(categoryRequestDto);
    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> update(
      @PathVariable Long id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
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
