package ua.kishkastrybaie.category;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(source = "parentCategory.name", target = "parentCategory")
  CategoryDto toDto(Category category);
}
