package ua.kishkastrybaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.repository.entity.Variation;

@Repository
public interface VariationRepository extends JpaRepository<Variation, Long> {}
