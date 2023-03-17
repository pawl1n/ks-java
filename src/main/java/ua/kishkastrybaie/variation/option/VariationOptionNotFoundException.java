package ua.kishkastrybaie.variation.option;

public class VariationOptionNotFoundException extends RuntimeException {
  public VariationOptionNotFoundException(VariationOptionId id) {
    super(
        "Variation option with variation id "
            + id.getVariation().getId()
            + " and value "
            + id.getValue()
            + " not found");
  }
}
