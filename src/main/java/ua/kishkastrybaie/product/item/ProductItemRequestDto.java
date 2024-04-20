package ua.kishkastrybaie.product.item;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record ProductItemRequestDto(
    @NotNull Integer stock,
    Set<VariationOptionRequestDto> variationOptions) {
  public record VariationOptionRequestDto(@NotNull String value, @NotNull Long variationId) {}
}
