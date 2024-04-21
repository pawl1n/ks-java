package ua.kishkastrybaie.product.item;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.kishkastrybaie.variation.option.VariationOption;
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
  @Mapping(target = "productName", source = "product.name")
  @Mapping(
      target = "description",
      source = "variationOptions",
      qualifiedByName = "constructDescription")
  ProductItemDto toDto(ProductItem productItem);

  @Named("constructDescription")
  default String constructDescription(Set<VariationOption> variationOptions) {
    return variationOptions.stream()
        .map(VariationOption::getValue)
        .collect(Collectors.joining(", "));
  }
}
