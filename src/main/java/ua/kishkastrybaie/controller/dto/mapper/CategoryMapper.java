package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.repository.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    List<CategoryDto> toDto(List<Category> categories);
    Category toDomain(CategoryDto categoryDto);
}
