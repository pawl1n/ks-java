package ua.kishkastrybaie.variation.option;

import org.springframework.hateoas.CollectionModel;

public interface VariationOptionService {
    CollectionModel<VariationOptionDto> getVariationOptions(Long id);

    VariationOptionDto getVariationOption(Long id, String value);

    VariationOptionDto createVariationOption(
            Long variationId, VariationOptionRequestDto variationOptionRequestDto);

    VariationOptionDto updateVariationOption(
            Long id, String value, VariationOptionRequestDto variationOptionRequestDto);

    void deleteVariationOption(Long variationId, String value);
}
