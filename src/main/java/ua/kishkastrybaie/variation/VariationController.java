package ua.kishkastrybaie.variation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
