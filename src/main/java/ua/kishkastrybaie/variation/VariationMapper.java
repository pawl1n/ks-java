package ua.kishkastrybaie.variation;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariationMapper {
  VariationDto toDto(Variation variation);

  Variation toDomain(VariationRequestDto variationRequestDto);
}
