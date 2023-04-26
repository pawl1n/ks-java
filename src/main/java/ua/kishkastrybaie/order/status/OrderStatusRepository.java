package ua.kishkastrybaie.order.status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatusHistory, Long> {}
