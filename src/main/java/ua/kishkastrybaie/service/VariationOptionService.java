package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.VariationOption;

public interface VariationOptionService {
    Iterable<VariationOption> findAll();
    VariationOption findById(Long id);
    VariationOption create(VariationOption variationOption);
    VariationOption update(Long id, VariationOption variationOption);
    VariationOption replace(Long id, VariationOption variationOption);
    void deleteById(Long id);
}
