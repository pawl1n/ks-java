package ua.kishkastrybaie.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.kishkastrybaie.order.status.OrderStatus;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Page<Order> findAllByUserEmail(String email, Pageable pageable);

  @Query(
      value =
          """
              select
               status,
               date(created_at) as date,
               count(*) as count
              from
               main.order
              where
               created_at between :startDate and :endDate
              group by date(created_at), status
              """,
      nativeQuery = true)
  List<IOrderCountReport> groupByCreatedAtBetween(Instant startDate, Instant endDate);

  Integer countByStatusAndCreatedAtBetween(OrderStatus status, Instant startDate, Instant endDate);

  @Query(
      value =
          "select sum(totalPrice) from Order where status = :status and createdAt between :startDate and :endDate")
  Double sumTotalPriceByStatusAndCreatedAtBetween(
      OrderStatus status, Instant startDate, Instant endDate);
}
