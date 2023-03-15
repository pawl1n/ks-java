package ua.kishkastrybaie.variation.option;

public class VariationOptionNotFoundException extends RuntimeException {
  public VariationOptionNotFoundException(Long id) {
    super("Variation option with id " + id + " not found");
  }
}
