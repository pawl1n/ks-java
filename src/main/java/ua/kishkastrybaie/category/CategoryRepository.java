package ua.kishkastrybaie.category;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query(
      value =
          """
                  SELECT id, name, slug, parent_category_id, path
                  FROM main.product_category
                  WHERE path > (SELECT path FROM main.product_category WHERE id = ?1)
                  """,
      nativeQuery = true)
  List<Category> findAllDescendantsById(Long id);

  @Query(
      value =
          """
                  SELECT EXISTS(
                       SELECT 1
                       FROM main.product_category c1, main.product_category c2
                       WHERE c1.id = ?1 AND c2.id = ?2 AND c1.path @> c2.path
                  )
                  """,
      nativeQuery = true)
  boolean isCategoryAncestorOfOther(Long categoryId, Long otherCategoryId);

  @Query(
      value =
          """
                  SELECT id, name, slug, parent_category_id, path
                  FROM main.product_category
                  WHERE parent_category_id IS NULL
                  """,
      nativeQuery = true)
  List<Category> findRootCategories();

  @Query(
      value =
          """
                  SELECT id, name, slug, parent_category_id, path
                  FROM main.product_category
                  WHERE public.ltree2text(path) = ?1
                  """,
      nativeQuery = true)
  Optional<Category> findByPath(String path);
}
