package ua.kishkastrybaie.image;

import jakarta.validation.constraints.NotBlank;

public record ImageRequestDto(
    @NotBlank String name, String description, @NotBlank String base64Image) {}
