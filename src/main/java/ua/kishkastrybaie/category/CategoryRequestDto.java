package ua.kishkastrybaie.category;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CategoryRequestDto(
    @NotBlank String name, Long parentCategory, String slug, List<Long> variations) {}
