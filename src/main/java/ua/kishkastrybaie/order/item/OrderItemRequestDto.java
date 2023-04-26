package ua.kishkastrybaie.order.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequestDto(@NotNull Long productItem, @NotNull @Positive Integer quantity) {}
