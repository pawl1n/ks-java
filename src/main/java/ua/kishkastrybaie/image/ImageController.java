package ua.kishkastrybaie.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
  private final ImageService imageService;

  @GetMapping
  public ResponseEntity<CollectionModel<ImageDto>> all() {
    log.info("Get all images");

    CollectionModel<ImageDto> responseDto = imageService.findAll();
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImageDto> one(@PathVariable Long id) {
    log.info("Get image by id: {}", id);

    ImageDto responseDto = imageService.findById(id);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping
  public ResponseEntity<ImageDto> save(@RequestBody ImageRequestDto imageRequestDto) {
    log.info("Save image: {}", imageRequestDto);

    ImageDto responseDto = imageService.create(imageRequestDto);
    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ImageDto> replace(
      @PathVariable Long id, @RequestBody ImageRequestDto imageRequestDto) {
    log.info("Replace image by id: {}", id);

    ImageDto responseDto = imageService.replace(id, imageRequestDto);

    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("Delete image by id: {}", id);

    imageService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
