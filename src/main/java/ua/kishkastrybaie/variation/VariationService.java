package ua.kishkastrybaie.variation;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.variation.option.VariationOptionDto;
import ua.kishkastrybaie.variation.option.VariationOptionRequestDto;

public interface VariationService {
  CollectionModel<VariationDto> findAll(Pageable pageable);

  VariationDto findById(Long id);

  VariationDto create(VariationRequestDto variationRequestDto);

  VariationDto replace(Long id, VariationRequestDto variationRequestDto);

  void deleteById(Long id);

  CollectionModel<VariationOptionDto> getVariationOptions(Long id);

  VariationOptionDto getVariationOption(Long id, String value);

  VariationOptionDto createVariationOption(
      Long variationId, VariationOptionRequestDto variationOptionRequestDto);

  VariationOptionDto updateVariationOption(
      Long id, String value, VariationOptionRequestDto variationOptionRequestDto);

  void deleteVariationOption(Long variationId, String value);
}
