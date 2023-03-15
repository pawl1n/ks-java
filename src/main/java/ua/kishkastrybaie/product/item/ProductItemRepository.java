package ua.kishkastrybaie.product.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.product.Product;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
  Iterable<ProductItem> findAllByProductId(Long id);

  Optional<ProductItem> findByIdAndProduct(Long id, Product product);
}
