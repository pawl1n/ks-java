package ua.kishkastrybaie.variation.option;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariationOptionMapper {
  @VariationOptionToVariationOptionDto
  @Mapping(target = "variationId", source = "variation.id")
  VariationOptionDto toDto(VariationOption variationOption);
}
