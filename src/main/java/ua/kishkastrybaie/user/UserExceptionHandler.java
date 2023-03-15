package ua.kishkastrybaie.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(EmailNotFoundException.class)
  public ResponseEntity<ErrorDto> emailNotFoundException(EmailNotFoundException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(e.getMessage()));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorDto> badCredentialsException(BadCredentialsException e) {
    return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
  }
}
