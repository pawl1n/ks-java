package ua.kishkastrybaie.product;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.category.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findAllByProductItemsIsNotNull(Pageable pageable);

  Optional<Product> findBySlug(String slug);

  @Query(
      value =
          """
                  SELECT p.* FROM main.product p JOIN main.product_category c ON p.category_id = c.id
                  WHERE c.path <@ public.text2ltree(:#{#category.path})
                  """,
      countQuery =
          """
                  SELECT COUNT(*) FROM main.product p JOIN main.product_category c ON p.category_id = c.id
                  WHERE c.path <@ public.text2ltree(:#{#category.path})
                  """,
      nativeQuery = true)
  Page<Product> findAllByCategory(@Param("category") Category category, Pageable pageable);
}
