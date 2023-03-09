package ua.kishkastrybaie.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(Long id) {
    super("User not found with id: " + id);
    log.error("User not found with id: " + id);
  }
}
