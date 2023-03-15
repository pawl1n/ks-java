package ua.kishkastrybaie.variation;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class VariationExceptionHandler {
  @ExceptionHandler(VariationNotFoundException.class)
  public ResponseEntity<ErrorDto> handleVariationNotFoundException(
      VariationNotFoundException exception) {
    return ResponseEntity.status(NOT_FOUND).body(new ErrorDto(exception.getMessage()));
  }
}
