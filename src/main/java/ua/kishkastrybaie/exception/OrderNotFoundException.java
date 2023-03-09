package ua.kishkastrybaie.exception;

public class OrderNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Order not found";

  public OrderNotFoundException() {
    super(MESSAGE);
  }
}
