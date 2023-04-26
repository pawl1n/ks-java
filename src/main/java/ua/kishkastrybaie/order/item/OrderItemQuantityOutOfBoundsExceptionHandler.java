package ua.kishkastrybaie.order.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.kishkastrybaie.shared.ErrorDto;

@RestControllerAdvice
public class OrderItemQuantityOutOfBoundsExceptionHandler {
  @ExceptionHandler(OrderItemQuantityOutOfBoundsException.class)
  public ResponseEntity<ErrorDto> handleOrderNotFoundException(
      OrderItemQuantityOutOfBoundsException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
  }
}
