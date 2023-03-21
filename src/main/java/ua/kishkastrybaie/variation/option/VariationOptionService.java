package ua.kishkastrybaie.variation.option;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

public interface VariationOptionService {
  CollectionModel<VariationOptionDto> findAllByVariationId(Long variationId, Pageable pageable);

  VariationOptionDto findByVariationIdAndValue(Long variationId, String value);

  VariationOptionDto create(Long variationId, VariationOptionRequestDto requestDto);

  VariationOptionDto replace(Long variationId, String value, VariationOptionRequestDto requestDto);

  void delete(Long variationId, String value);
}
