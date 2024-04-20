package ua.kishkastrybaie.variation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationRepository extends JpaRepository<Variation, Long> {
    List<Variation> findAllByCategoriesId(Long productCategoryId);
}
