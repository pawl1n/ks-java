package ua.kishkastrybaie.variation;

public class VariationNotFoundException extends RuntimeException {

  public VariationNotFoundException(Long id) {
    super("Variation with id " + id + " not found");
  }
}
