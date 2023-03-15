package ua.kishkastrybaie.product.item;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record ProductItemRequestDto(
    @NotNull Double price,
    @NotNull Integer stock,
    @NotNull String sku,
    Set<VariationOptionRequestDto> variationOptions) {
  public record VariationOptionRequestDto(@NotNull String value, @NotNull Long variationId) {}
}
