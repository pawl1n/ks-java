package ua.kishkastrybaie.shared;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SQLGrammarExceptionHandler {
  @ExceptionHandler(SQLGrammarException.class)
  public ResponseEntity<ErrorDto> handleEntityNotFoundException(SQLGrammarException e) {
    return ResponseEntity.internalServerError().body(new ErrorDto("Internal server error"));
  }
}
