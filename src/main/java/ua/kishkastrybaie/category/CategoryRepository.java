package ua.kishkastrybaie.category;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query(
      value =
          """
                  SELECT id, name, parent_category_id, path
                  FROM main.product_category
                  WHERE path > (SELECT path FROM main.product_category WHERE id = ?1)
                  """,
      nativeQuery = true)
  List<Category> findAllDescendantsById(Long id);
}
