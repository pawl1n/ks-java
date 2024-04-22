package ua.kishkastrybaie.search;

import jakarta.validation.constraints.NotBlank;

public record SearchRequestDto(@NotBlank String q) {}
