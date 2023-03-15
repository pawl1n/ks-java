package ua.kishkastrybaie.shared;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PersistenceExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
    return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
  }
}
