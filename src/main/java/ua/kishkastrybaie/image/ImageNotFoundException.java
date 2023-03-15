package ua.kishkastrybaie.image;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException(Long id) {
    super("Image not found with id: " + id);
    log.error(getMessage());
  }

  public ImageNotFoundException(String message) {
      super(message);
  }
}
