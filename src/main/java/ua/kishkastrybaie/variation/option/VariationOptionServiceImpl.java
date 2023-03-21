package ua.kishkastrybaie.variation.option;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationNotFoundException;
import ua.kishkastrybaie.variation.VariationRepository;

@Service
@RequiredArgsConstructor
public class VariationOptionServiceImpl implements VariationOptionService {
  private final VariationOptionRepository variationOptionRepository;
  private final VariationOptionModelAssembler variationOptionModelAssembler;
  private final PagedResourcesAssembler<VariationOption> pagedResourcesAssembler;
  private final VariationRepository variationRepository;

  @Override
  public CollectionModel<VariationOptionDto> findAllByVariationId(
      Long variationId, Pageable pageable) {
    if (!variationRepository.existsById(variationId)) {
        throw new VariationNotFoundException(variationId);
    }

    return pagedResourcesAssembler.toModel(
        variationOptionRepository.findAllByVariationId(variationId, pageable),
        variationOptionModelAssembler);
  }

  @Override
  public VariationOptionDto findByVariationIdAndValue(Long variationId, String value) {
    if (!variationRepository.existsById(variationId)) {
      throw new VariationNotFoundException(variationId);
    }
    Variation variation = variationRepository.getReferenceById(variationId);

    return variationOptionModelAssembler.toModel(
        variationOptionRepository
            .findById(new VariationOptionId(variation, value))
            .orElseThrow(
                () ->
                    new VariationOptionNotFoundException(new VariationOptionId(variation, value))));
  }

  @Override
  public VariationOptionDto create(
      Long variationId, VariationOptionRequestDto variationOptionRequestDto) {
    if (!variationRepository.existsById(variationId)) {
      throw new VariationNotFoundException(variationId);
    }
    Variation variation = variationRepository.getReferenceById(variationId);

    VariationOption variationOptionRequest = new VariationOption();
    variationOptionRequest.setVariation(variation);
    variationOptionRequest.setValue(variationOptionRequestDto.value());

    return variationOptionModelAssembler.toModel(
        variationOptionRepository.save(variationOptionRequest));
  }

  @Override
  public VariationOptionDto replace(
      Long variationId, String value, VariationOptionRequestDto variationOptionRequestDto) {
    if (!variationRepository.existsById(variationId)) {
      throw new VariationNotFoundException(variationId);
    }

    Variation variation = variationRepository.getReferenceById(variationId);
    VariationOption variationOption =
        variationOptionRepository
            .findById(new VariationOptionId(variation, value))
            .map(
                v -> {
                  v.setValue(variationOptionRequestDto.value());
                  return variationOptionRepository.save(v);
                })
            .orElseThrow(
                () ->
                    new VariationOptionNotFoundException(new VariationOptionId(variation, value)));

    return variationOptionModelAssembler.toModel(variationOptionRepository.save(variationOption));
  }

  @Override
  public void delete(Long variationId, String value) {
    if (!variationRepository.existsById(variationId)) {
      throw new VariationNotFoundException(variationId);
    }
    Variation variation = variationRepository.getReferenceById(variationId);

    VariationOptionId variationOptionId = new VariationOptionId(variation, value);
    if (!variationOptionRepository.existsById(variationOptionId)) {
      throw new VariationOptionNotFoundException(variationOptionId);
    }

    variationOptionRepository.deleteById(variationOptionId);
  }
}
