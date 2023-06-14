package ua.kishkastrybaie.order.item;

public class OrderItemQuantityOutOfBoundsException extends RuntimeException {
  public OrderItemQuantityOutOfBoundsException(Long id, int quantity) {
    super(String.format("Order item with id %d has quantity %d", id, quantity));
  }
}
