package ua.kishkastrybaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.repository.entity.VariationOption;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOption, Long> {
}
