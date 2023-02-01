package ua.kishkastrybaie.controller.dto;

public record ProductDto(
        Long id,
        String name,
        String description,
        String category,
        String mainImage
) {
}
