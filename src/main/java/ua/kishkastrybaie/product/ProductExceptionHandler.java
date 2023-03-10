package ua.kishkastrybaie.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class ProductExceptionHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorDto> handleProductNotFound(ProductNotFoundException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
  }
}
