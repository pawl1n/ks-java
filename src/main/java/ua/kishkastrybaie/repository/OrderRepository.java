package ua.kishkastrybaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kishkastrybaie.repository.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
