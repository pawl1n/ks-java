package ua.kishkastrybaie.variation;

import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariationMapper {
  VariationDto toDto(Variation variation);

  List<VariationDto> toDto(List<Variation> variations);

  Variation toDomain(VariationDto variationDto);
}
