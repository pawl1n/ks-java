package ua.kishkastrybaie.service.implementation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.exception.VariationNotFoundException;
import ua.kishkastrybaie.repository.VariationRepository;
import ua.kishkastrybaie.repository.entity.Variation;
import ua.kishkastrybaie.service.CategoryService;
import ua.kishkastrybaie.service.VariationService;

@Service
@RequiredArgsConstructor
public class VariationServiceImpl implements VariationService {
  private final VariationRepository variationRepository;
  private final CategoryService categoryService;

  @Override
  public List<Variation> findAll() {
    return variationRepository.findAll();
  }

  @Override
  public Variation findById(Long id) {
    return variationRepository.findById(id).orElseThrow(VariationNotFoundException::new);
  }

  @Override
  public Variation create(Variation variation) {
    return variationRepository.save(variation);
  }

  @Override
  public Variation replace(Long id, Variation newVariation) {
    return variationRepository
        .findById(id)
        .map(
            variation -> {
              variation.setName(newVariation.getName());
              return variationRepository.save(variation);
            })
        .orElseGet(
            () -> {
              newVariation.setId(id);
              return variationRepository.save(newVariation);
            });
  }

  @Override
  public void deleteById(Long id) {
    Variation variation = findById(id);
    variationRepository.delete(variation);
  }
}
