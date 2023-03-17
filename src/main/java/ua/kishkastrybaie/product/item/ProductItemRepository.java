package ua.kishkastrybaie.product.item;

import java.util.Optional;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
  Page<ProductItem> findAllByProductId(Long productId, Pageable pageable);

  Optional<ProductItem> findByIdAndProductId(Long id, Long productId);
}
