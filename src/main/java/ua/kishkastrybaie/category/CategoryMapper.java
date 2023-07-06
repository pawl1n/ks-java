package ua.kishkastrybaie.category;

import java.util.List;
import java.util.Set;
import org.mapstruct.*;
import ua.kishkastrybaie.category.tree.CategoryTreeDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category category);

  @Mapping(target = "descendants", qualifiedByName = "mapDescendants")
  CategoryTreeDto toTreeDto(Category category);

  @Named("mapDescendants")
  default List<CategoryTreeDto> mapDescendants(Set<Category> categories) {
    return categories.stream().map(this::toTreeDto).toList();
  }
}
