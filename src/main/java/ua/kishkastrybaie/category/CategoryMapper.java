package ua.kishkastrybaie.category;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category category);
}
