package ua.kishkastrybaie.product;

import jakarta.validation.constraints.NotBlank;
import java.net.URL;

public record ProductRequestDto(
    @NotBlank String name, String description, Long category, URL mainImage, String slug, String sku, Double price) {}
