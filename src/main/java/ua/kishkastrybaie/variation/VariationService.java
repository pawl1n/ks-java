package ua.kishkastrybaie.variation;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

public interface VariationService {
  CollectionModel<VariationDto> findAll(Pageable pageable);

  VariationDto findById(Long id);

  VariationDto create(VariationRequestDto variationRequestDto);

  VariationDto replace(Long id, VariationRequestDto variationRequestDto);

  void deleteById(Long id);

 }
