package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.*;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.controller.dto.ProductRequestDto;
import ua.kishkastrybaie.repository.entity.Product;

@Mapper(
    componentModel = "spring",
    uses = {CategoryIdToCategoryMapperImpl.class})
public interface ProductMapper {

  @Mapping(source = "category.name", target = "category")
  ProductDto toDto(Product product);

  @Mapping(target = "id", ignore = true)
  @Mapping(
      target = "category",
      qualifiedBy = {CategoryIdToCategory.class})
  Product toDomain(ProductRequestDto productRequestDto);
}
