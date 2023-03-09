package ua.kishkastrybaie.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(String message) {
    super(message);
  }

  public ProductNotFoundException(Long id) {
    super("Product not found with id: " + id);
    log.error("Product not found with id: " + id);
  }
}
