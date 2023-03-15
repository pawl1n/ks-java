package ua.kishkastrybaie.variation;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.variation.option.VariationOptionDto;
import ua.kishkastrybaie.variation.option.VariationOptionRequestDto;

@RestController
@RequestMapping("/api/variations")
@RequiredArgsConstructor
public class VariationController {
  private final VariationService variationService;

  @GetMapping
  public ResponseEntity<CollectionModel<VariationDto>> all(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(variationService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<VariationDto> one(@PathVariable Long id) {
    return ResponseEntity.ok(variationService.findById(id));
  }

  @PostMapping
  public ResponseEntity<VariationDto> create(@RequestBody VariationRequestDto variationRequestDto) {
    VariationDto variationDto = variationService.create(variationRequestDto);

    return ResponseEntity.created(variationDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(variationDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<VariationDto> replace(
      @PathVariable Long id, @RequestBody VariationRequestDto variationRequestDto) {
    VariationDto variationDto = variationService.replace(id, variationRequestDto);

    return ResponseEntity.ok(variationDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    variationService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/options")
  public ResponseEntity<CollectionModel<VariationOptionDto>> options(@PathVariable Long id) {
    return ResponseEntity.ok(variationService.getVariationOptions(id));
  }

  @GetMapping("/{id}/options/{value}")
  public ResponseEntity<VariationOptionDto> option(
      @PathVariable Long id, @PathVariable @NotNull String value) {
    return ResponseEntity.ok(variationService.getVariationOption(id, value));
  }

  @PostMapping("/{id}/options")
  public ResponseEntity<VariationOptionDto> createOption(
      @PathVariable Long id, @RequestBody VariationOptionRequestDto variationOptionRequestDto) {

    VariationOptionDto variationOptionDto =
        variationService.createVariationOption(id, variationOptionRequestDto);

    return ResponseEntity.created(
            variationOptionDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(variationOptionDto);
  }

  @PutMapping("/{id}/options/{value}")
  public ResponseEntity<VariationOptionDto> updateOption(
      @PathVariable Long id,
      @PathVariable @NotNull String value,
      @RequestBody VariationOptionRequestDto variationOptionRequestDto) {

    VariationOptionDto variationOptionDto =
        variationService.updateVariationOption(id, value, variationOptionRequestDto);

    return ResponseEntity.ok(variationOptionDto);
  }

  @DeleteMapping("/{id}/options/{value}")
  public ResponseEntity<Void> deleteOption(
      @PathVariable Long id, @PathVariable @NotNull String value) {
    variationService.deleteVariationOption(id, value);

    return ResponseEntity.noContent().build();
  }
}
