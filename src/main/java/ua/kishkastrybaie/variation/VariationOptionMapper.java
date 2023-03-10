package ua.kishkastrybaie.variation;

import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariationOptionMapper {
  VariationOptionDto toDto(VariationOption variationOption);

  List<VariationOptionDto> toDto(List<VariationOption> variationOptions);

  VariationOption toDomain(VariationOptionDto variationOptionDto);
}
