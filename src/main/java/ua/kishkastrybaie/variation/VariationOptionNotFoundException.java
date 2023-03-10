package ua.kishkastrybaie.variation;

public class VariationOptionNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Variation option not found";

  public VariationOptionNotFoundException() {
    super(MESSAGE);
  }
}
