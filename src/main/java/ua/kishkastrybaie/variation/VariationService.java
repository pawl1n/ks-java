package ua.kishkastrybaie.variation;


public interface VariationService {
  Iterable<Variation> findAll();

  Variation findById(Long id);

  Variation create(Variation variation);

  Variation replace(Long id, Variation variation);

  void deleteById(Long id);
}
