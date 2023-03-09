package ua.kishkastrybaie.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.controller.dto.ErrorDto;
import ua.kishkastrybaie.exception.CategoryNotFoundException;

@RestControllerAdvice
public class CategoryExceptionHandler {

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorDto> handleCategoryNotFound(CategoryNotFoundException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
  }
}
