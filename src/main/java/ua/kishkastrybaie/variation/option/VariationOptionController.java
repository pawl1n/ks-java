package ua.kishkastrybaie.variation.option;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<CollectionModel<VariationOptionDto>> all(@PathVariable Long variationId) {
    return ResponseEntity.ok(variationOptionService.getVariationOptions(variationId));
  }

  @GetMapping("/{value}")
  public ResponseEntity<VariationOptionDto> one(
      @PathVariable Long variationId, @PathVariable @NotNull String value) {
    return ResponseEntity.ok(variationOptionService.getVariationOption(variationId, value));
  }

  @PostMapping
  public ResponseEntity<VariationOptionDto> save(
      @PathVariable Long variationId,
      @RequestBody VariationOptionRequestDto variationOptionRequestDto) {

    VariationOptionDto variationOptionDto =
        variationOptionService.createVariationOption(variationId, variationOptionRequestDto);

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
        variationOptionService.updateVariationOption(variationId, value, variationOptionRequestDto);

    return ResponseEntity.ok(variationOptionDto);
  }

  @DeleteMapping("/{value}")
  public ResponseEntity<Void> delete(
      @PathVariable Long variationId, @PathVariable @NotNull String value) {
    variationOptionService.deleteVariationOption(variationId, value);

    return ResponseEntity.noContent().build();
    }
}
