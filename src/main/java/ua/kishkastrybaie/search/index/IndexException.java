package ua.kishkastrybaie.search.index;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexException extends RuntimeException {
  public IndexException(String message, Exception e) {
    super("Error while indexing: " + message + "(" + e.getMessage() + ")");
    log.info("Error while indexing: " + message + "(" + e.getMessage() + ")");
  }
}
