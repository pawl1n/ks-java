package ua.kishkastrybaie.product.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kishkastrybaie.variation.option.VariationOptionMapper;
import ua.kishkastrybaie.variation.option.VariationOptionToVariationOptionDto;

@Mapper(
    componentModel = "spring",
    uses = {VariationOptionMapper.class})
public interface ProductItemMapper {
  @Mapping(
      target = "variationOptions",
      source = "variationOptions",
      qualifiedBy = {VariationOptionToVariationOptionDto.class})
  ProductItemDto toDto(ProductItem productItem);
}
