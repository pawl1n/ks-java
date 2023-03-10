package ua.kishkastrybaie.product;

import org.mapstruct.*;
import ua.kishkastrybaie.shared.mapper.CategoryIdToCategory;
import ua.kishkastrybaie.shared.mapper.CategoryIdToCategoryMapperImpl;

@Mapper(
    componentModel = "spring",
    uses = {CategoryIdToCategoryMapperImpl.class})
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper {

  @Mapping(source = "category.name", target = "category")
  @Mapping(source = "mainImage.url", target = "mainImage")
  ProductDto toDto(Product product);

  @Mapping(target = "id", ignore = true)
      qualifiedBy = {ImageURLToImage.class})
  @Mapping(
      target = "category",
      qualifiedBy = {CategoryIdToCategory.class})
  Product toDomain(ProductRequestDto productRequestDto);
}
