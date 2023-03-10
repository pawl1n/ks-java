package ua.kishkastrybaie.variation;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.category.CategoryDto;
import ua.kishkastrybaie.shared.ErrorDto;

@RestController
@RequestMapping("/api/v1/variations")
@RequiredArgsConstructor
public class VariationController {
  private final VariationService variationService;
  private final VariationMapper variationMapper;
  private final RepresentationModelAssembler<Variation, VariationDto> variationModelAssembler;
  private final RepresentationModelAssembler<Category, CategoryDto> categoryModelAssembler;

  @GetMapping
  public ResponseEntity<CollectionModel<VariationDto>> all() {
    CollectionModel<VariationDto> responseDto =
        variationModelAssembler.toCollectionModel(variationService.findAll());
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<VariationDto> one(@PathVariable Long id) {
    VariationDto responseDto = variationModelAssembler.toModel(variationService.findById(id));
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}/category")
  public ResponseEntity<CategoryDto> category(@PathVariable Long id) {
    CategoryDto responseDto =
        categoryModelAssembler.toModel(variationService.findById(id).getCategory());
    return ResponseEntity.ok(responseDto);
  }

  @ExceptionHandler(VariationNotFoundException.class)
  public ResponseEntity<ErrorDto> handleVariationNotfound(VariationNotFoundException e) {
    return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
  }
}
