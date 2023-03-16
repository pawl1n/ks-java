package ua.kishkastrybaie.variation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariationServiceImpl implements VariationService {
  private final VariationRepository variationRepository;
  private final RepresentationModelAssembler<Variation, VariationDto> variationModelAssembler;
  private final VariationMapper variationMapper;
  private final PagedResourcesAssembler<Variation> pagedResourcesAssembler;

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
}
