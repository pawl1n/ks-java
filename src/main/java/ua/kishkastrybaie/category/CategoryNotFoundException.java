package ua.kishkastrybaie.category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(String message) {
    super(message);
    log.error(getMessage());
  }

  public CategoryNotFoundException(Long id) {
    super("Category not found with id: " + id);
    log.error(getMessage());
  }
}
