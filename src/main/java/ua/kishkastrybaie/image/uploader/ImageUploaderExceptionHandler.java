package ua.kishkastrybaie.image.uploader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class ImageUploaderExceptionHandler {
  @ExceptionHandler(ImageUploadException.class)
  public ResponseEntity<ErrorDto> handleImageUploadException(ImageUploadException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
  }
}
