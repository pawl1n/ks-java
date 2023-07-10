package ua.kishkastrybaie.product;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.category.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findAllByProductItemsIsNotNull(Pageable pageable);

  Optional<Product> findBySlug(String slug);

  Page<Product> findAllByCategory(Category category, Pageable pageable);
}
