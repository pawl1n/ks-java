package ua.kishkastrybaie.order.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class OrderStatusExceptionHandler {
  @ExceptionHandler(IllegalOrderStatusException.class)
  public ResponseEntity<ErrorDto> handleIllegalOrderStatus(IllegalOrderStatusException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
  }
}
