package ua.kishkastrybaie.variation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.variation.option.*;

@Service
@RequiredArgsConstructor
public class VariationServiceImpl implements VariationService {
  private final VariationRepository variationRepository;
  private final RepresentationModelAssembler<Variation, VariationDto> variationModelAssembler;
  private final VariationMapper variationMapper;
  private final PagedResourcesAssembler<Variation> pagedResourcesAssembler;
  private final RepresentationModelAssembler<VariationOption, VariationOptionDto>
      variationOptionModelAssembler;
  private final VariationOptionRepository variationOptionRepository;

  private final VariationOptionMapper variationOptionMapper;

  @Override
  public CollectionModel<VariationDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(
        variationRepository.findAll(pageable), variationModelAssembler);
  }

  @Override
  public VariationDto findById(Long id) {
    return variationModelAssembler.toModel(
        variationRepository.findById(id).orElseThrow(() -> new VariationNotFoundException(id)));
  }

  @Override
  public VariationDto create(VariationRequestDto variationRequestDto) {
    Variation variation = variationRepository.save(variationMapper.toDomain(variationRequestDto));

    return variationModelAssembler.toModel(variationRepository.save(variation));
  }

  @Override
  public VariationDto replace(Long id, VariationRequestDto variationRequestDto) {
    Variation variation =
        variationRepository
            .findById(id)
            .map(
                v -> {
                  v.setName(variationRequestDto.name());
                  return variationRepository.save(v);
                })
            .orElseThrow(() -> new VariationNotFoundException(id));

    return variationModelAssembler.toModel(variation);
  }

  @Override
  public void deleteById(Long id) {
    Variation variation = variationRepository.getReferenceById(id);
    variationRepository.delete(variation);
  }

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
