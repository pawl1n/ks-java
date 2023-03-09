package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.*;
import ua.kishkastrybaie.controller.dto.CategoryDto;
import ua.kishkastrybaie.controller.dto.CategoryRequestDto;
import ua.kishkastrybaie.repository.entity.Category;

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
