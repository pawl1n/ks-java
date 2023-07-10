package ua.kishkastrybaie.product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(Long id) {
    super("Product not found with id: " + id);
    log.error(getMessage());
  }

  public ProductNotFoundException(String message) {
    super(message);
    log.error(getMessage());
  }
}
