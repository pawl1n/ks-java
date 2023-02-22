package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.VariationDto;
import ua.kishkastrybaie.repository.entity.Variation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VariationMapper {
    VariationDto toDto(Variation variation);
    List<VariationDto> toDto(List<Variation> variations);
    Variation toDomain(VariationDto variationDto);
}
