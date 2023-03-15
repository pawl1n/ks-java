package ua.kishkastrybaie.variation.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOption, VariationOptionId> {
    Iterable<VariationOption> findAllByVariationId(Long variationId);
}
