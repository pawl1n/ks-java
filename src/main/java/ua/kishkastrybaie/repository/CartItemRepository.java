package ua.kishkastrybaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.repository.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
