package ua.kishkastrybaie.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(@NotBlank String name, Long parentCategory, String slug) {}
