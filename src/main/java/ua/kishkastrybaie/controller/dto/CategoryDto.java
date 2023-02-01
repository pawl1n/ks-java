package ua.kishkastrybaie.controller.dto;

public record CategoryDto(
        Long id,
        String name,
        CategoryDto parentCategory
) {

}
