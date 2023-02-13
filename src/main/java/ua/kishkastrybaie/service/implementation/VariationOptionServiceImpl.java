package ua.kishkastrybaie.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.exception.VariationOptionNotFoundException;
import ua.kishkastrybaie.repository.VariationOptionRepository;
import ua.kishkastrybaie.repository.entity.VariationOption;
import ua.kishkastrybaie.service.VariationOptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariationOptionServiceImpl implements VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;

    @Override
    public List<VariationOption> findAll() {
        return variationOptionRepository.findAll();
    }

    @Override
    public VariationOption findById(Long id) {
        return variationOptionRepository.findById(id)
                .orElseThrow(VariationOptionNotFoundException::new);
    }

    @Override
    public VariationOption create(VariationOption variationOption) {
        return variationOptionRepository.save(variationOption);
    }

    @Override
    public VariationOption update(Long id, VariationOption variationOptionDetails) {
        VariationOption variation = variationOptionRepository.findById(id).
                orElseThrow(VariationOptionNotFoundException::new);

        if (variationOptionDetails.getValue() != null) {
            variation.setValue(variation.getValue());
        }

        return variationOptionRepository.save(variation);
    }

    @Override
    public VariationOption replace(Long id, VariationOption variationDetails) {
        return variationOptionRepository.findById(id).map(variationOption -> {
            variationOption.setValue(variationDetails.getValue());
            return variationOptionRepository.save(variationOption);
        }).orElseGet(() -> {
            variationDetails.setId(id);
            return variationOptionRepository.save(variationDetails);
        });
    }

    @Override
    public void deleteById(Long id) {
        VariationOption variationOption = variationOptionRepository.findById(id)
                .orElseThrow(VariationOptionNotFoundException::new);

        variationOptionRepository.delete(variationOption);
    }
}
