package ua.kishkastrybaie.variation.option;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.variation.Variation;
import ua.kishkastrybaie.variation.VariationNotFoundException;
import ua.kishkastrybaie.variation.VariationRepository;

@Service
@RequiredArgsConstructor
public class VariationOptionServiceImpl implements VariationOptionService {
  private final VariationOptionRepository variationOptionRepository;
  private final VariationOptionMapper variationOptionMapper;
  private final RepresentationModelAssembler<VariationOption, VariationOptionDto>
      variationOptionModelAssembler;
  private final VariationRepository variationRepository;

  @Override
  public CollectionModel<VariationOptionDto> getVariationOptions(Long id) {
    return variationOptionModelAssembler.toCollectionModel(
        variationOptionRepository.findAllByVariationId(id));
  }

  @Override
  public VariationOptionDto getVariationOption(Long id, String value) {
    Variation variation = variationRepository.getReferenceById(id);

    return variationOptionModelAssembler.toModel(
        variationOptionRepository
            .findById(new VariationOptionId(variation, value))
            .orElseThrow(() -> new VariationOptionNotFoundException(id)));
  }

  @Override
  public VariationOptionDto createVariationOption(
      Long variationId, VariationOptionRequestDto variationOptionRequestDto) {
    Variation variation =
        variationRepository
            .findById(variationId)
            .orElseThrow(() -> new VariationNotFoundException(variationId));

    VariationOption variationOptionRequest =
        variationOptionMapper.toDomain(variationOptionRequestDto);
    variationOptionRequest.setVariation(variation);

    return variationOptionModelAssembler.toModel(
        variationOptionRepository.save(variationOptionRequest));
  }

  @Override
  public VariationOptionDto updateVariationOption(
      Long id, String value, VariationOptionRequestDto variationOptionRequestDto) {
    Variation variation = variationRepository.getReferenceById(id);
    VariationOption variationOption =
        variationOptionRepository
            .findById(new VariationOptionId(variation, value))
            .map(
                v -> {
                  v.setValue(variationOptionRequestDto.value());
                  return variationOptionRepository.save(v);
                })
            .orElseThrow(() -> new VariationOptionNotFoundException(id));

    return variationOptionModelAssembler.toModel(variationOptionRepository.save(variationOption));
  }

  @Override
  public void deleteVariationOption(Long id, String value) {
    Variation variation = variationRepository.getReferenceById(id);
    VariationOption variationOption =
        variationOptionRepository.getReferenceById(new VariationOptionId(variation, value));

    variationOptionRepository.delete(variationOption);
  }
}
