package ua.kishkastrybaie.category;

import org.mapstruct.*;
import ua.kishkastrybaie.shared.mapper.CategoryIdToCategory;
import ua.kishkastrybaie.shared.mapper.CategoryIdToCategoryMapperImpl;

@Mapper(
    componentModel = "spring",
    uses = {CategoryIdToCategoryMapperImpl.class})
public interface CategoryMapper {

  @Mapping(source = "parentCategory.name", target = "parentCategory")
  CategoryDto toDto(Category category);

  @Mapping(
      target = "parentCategory",
      qualifiedBy = {CategoryIdToCategory.class})
  Category toDomain(CategoryRequestDto categoryDto);
}
