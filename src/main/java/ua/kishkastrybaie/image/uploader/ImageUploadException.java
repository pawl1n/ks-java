package ua.kishkastrybaie.image.uploader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUploadException extends RuntimeException {
  public ImageUploadException(String message) {
    super("Error while uploading image: " + message);
    log.error(getMessage());
  }
}
