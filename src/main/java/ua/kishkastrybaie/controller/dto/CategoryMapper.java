package ua.kishkastrybaie.controller.dto;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.repository.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto(Category category);
    Category toDomain(CategoryDTO categoryDTO);
}
