package ua.kishkastrybaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.repository.entity.ProductItem;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {}
