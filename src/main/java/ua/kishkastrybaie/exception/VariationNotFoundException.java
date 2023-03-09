package ua.kishkastrybaie.exception;

public class VariationNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Variation not found";

  public VariationNotFoundException() {
    super(MESSAGE);
  }
}
