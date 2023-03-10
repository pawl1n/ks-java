package ua.kishkastrybaie.product;

public class ProductItemNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Product item not found";

  public ProductItemNotFoundException() {
    super(MESSAGE);
  }
}
