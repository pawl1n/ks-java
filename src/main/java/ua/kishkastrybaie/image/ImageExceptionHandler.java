package ua.kishkastrybaie.image;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class ImageExceptionHandler {

  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<ErrorDto> handleImageDecodeException(ImageNotFoundException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
  }

  @ExceptionHandler(ImageNameAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handleImageNameAlreadyExistsException(ImageNameAlreadyExistsException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
  }
}
