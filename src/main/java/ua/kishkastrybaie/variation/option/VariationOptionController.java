package ua.kishkastrybaie.variation.option;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variations/{variationId}/options")
@RequiredArgsConstructor
public class VariationOptionController {
  private final VariationOptionService variationOptionService;

  @GetMapping
  public ResponseEntity<CollectionModel<VariationOptionDto>> all(
      @PathVariable Long variationId, @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(variationOptionService.findAllByVariationId(variationId, pageable));
  }

  @GetMapping("/{value}")
  public ResponseEntity<VariationOptionDto> one(
      @PathVariable Long variationId, @PathVariable @NotNull String value) {
    return ResponseEntity.ok(variationOptionService.findByVariationIdAndValue(variationId, value));
  }

  @PostMapping
  public ResponseEntity<VariationOptionDto> save(
      @PathVariable Long variationId,
      @RequestBody VariationOptionRequestDto variationOptionRequestDto) {

    VariationOptionDto variationOptionDto =
        variationOptionService.create(variationId, variationOptionRequestDto);

    return ResponseEntity.created(
            variationOptionDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(variationOptionDto);
  }

  @PutMapping("/{value}")
  public ResponseEntity<VariationOptionDto> replace(
      @PathVariable Long variationId,
      @PathVariable @NotNull String value,
      @RequestBody VariationOptionRequestDto variationOptionRequestDto) {

    VariationOptionDto variationOptionDto =
        variationOptionService.replace(variationId, value, variationOptionRequestDto);

    return ResponseEntity.ok(variationOptionDto);
  }

  @DeleteMapping("/{value}")
  public ResponseEntity<Void> delete(
      @PathVariable Long variationId, @PathVariable @NotNull String value) {
    variationOptionService.delete(variationId, value);

    return ResponseEntity.noContent().build();
  }
}
