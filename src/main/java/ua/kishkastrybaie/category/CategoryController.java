package ua.kishkastrybaie.category;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.category.tree.CategoryTreeDto;
import ua.kishkastrybaie.variation.VariationDto;
import ua.kishkastrybaie.variation.VariationService;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
  private final CategoryService categoryService;
  private final VariationService variationService;

  @GetMapping
  public ResponseEntity<CollectionModel<CategoryDto>> all(@PageableDefault Pageable pageable) {
    log.info("Get all categories");

    CollectionModel<CategoryDto> responseDto =
        categoryService
            .findAll(pageable)
            .add(linkTo(methodOn(CategoryController.class).all(pageable)).withSelfRel());
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/root")
  public ResponseEntity<CollectionModel<CategoryDto>> root() {
    log.info("Get root categories");

    CollectionModel<CategoryDto> responseDto =
        categoryService
            .findRootCategories()
            .add(linkTo(methodOn(CategoryController.class).root()).withSelfRel());
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/tree")
  public ResponseEntity<CollectionModel<CategoryTreeDto>> tree() {
    log.info("Get root categories");

    return ResponseEntity.ok(categoryService.getTree());
  }

  @GetMapping("/path/{*path}")
  public ResponseEntity<CategoryTreeDto> one(@PathVariable(name = "path") String path) {
    log.info("Get category by path: {}", path);

    return ResponseEntity.ok(categoryService.findByPath(path));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> one(@PathVariable Long id) {
    log.info("Get category by id: {}", id);

    CategoryDto responseDto = categoryService.findById(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/descendants")
  public ResponseEntity<CollectionModel<CategoryDto>> descendants(@PathVariable Long id) {
    log.info("Get category descendants by id: {}", id);

    CollectionModel<CategoryDto> responseDto = categoryService.findAllDescendants(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/variations")
  public ResponseEntity<CollectionModel<VariationDto>> variations(@PathVariable Long id) {
    log.info("Get category variations by id: {}", id);

    CollectionModel<VariationDto> responseDto = variationService.findAllVariationsByCategory(id);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> save(
      @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    log.info("Save category: {}", categoryRequestDto);

    CategoryDto responseDto = categoryService.create(categoryRequestDto);
    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .build();
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
