package ua.kishkastrybaie.category;

import java.util.List;
import java.util.Set;
import org.mapstruct.*;
import ua.kishkastrybaie.category.tree.CategoryTreeDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  @Mapping(target = "path", qualifiedByName = "mapToApiPath")
  @Mapping(target = "parentCategory", source = "parentCategory.id")
  CategoryDto toDto(Category category);

  @Mapping(target = "descendants", qualifiedByName = "mapDescendants")
  @Mapping(target = "path", qualifiedByName = "mapToApiPath")
  CategoryTreeDto toTreeDto(Category category);

  @Named("mapDescendants")
  default List<CategoryTreeDto> mapDescendants(Set<Category> categories) {
    return categories.stream().map(this::toTreeDto).toList();
  }

  @Named("mapToApiPath")
  default String mapToApiPath(String path) {
    if (path == null) {
      return null;
    }

    return path.replace(".", "/");
  }
}
