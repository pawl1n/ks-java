package ua.kishkastrybaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.repository.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
