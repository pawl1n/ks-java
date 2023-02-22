package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.Variation;

public interface VariationService {
    Iterable<Variation> findAll();
    Variation findById(Long id);
    Variation create(Variation variation);
    Variation update(Long id, Variation variation);
    Variation replace(Long id, Variation variation);
    void deleteById(Long id);
}
