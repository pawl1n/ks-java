package ua.kishkastrybaie.controller.dto.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.VariationOptionDto;
import ua.kishkastrybaie.repository.entity.VariationOption;

@Mapper(componentModel = "spring")
public interface VariationOptionMapper {
  VariationOptionDto toDto(VariationOption variationOption);

  List<VariationOptionDto> toDto(List<VariationOption> variationOptions);

  VariationOption toDomain(VariationOptionDto variationOptionDto);
}
