package ua.kishkastrybaie.variation.option;

import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOption, VariationOptionId> {
    Page<VariationOption> findAllByVariationId(Long variationId, Pageable pageable);
}
