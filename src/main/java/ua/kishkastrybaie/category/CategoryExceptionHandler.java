package ua.kishkastrybaie.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class CategoryExceptionHandler {

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorDto> handleCategoryNotFound(CategoryNotFoundException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
  }

  @ExceptionHandler(CategoryHasDescendantsException.class)
  public ResponseEntity<ErrorDto> handleCategoryHasDescendantsException(CategoryHasDescendantsException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
  }

  @ExceptionHandler(CyclicCategoryPathException.class)
  public ResponseEntity<ErrorDto> handleCyclicCategoryPathException(CyclicCategoryPathException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
  }
}
