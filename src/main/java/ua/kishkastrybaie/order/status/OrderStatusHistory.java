package ua.kishkastrybaie.order.status;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.order.Order;

@Entity
@Table(name = "order_status", schema = "main")
@Getter
@Setter
public class OrderStatusHistory {
  @Id
  @GeneratedValue(generator = "order_status_seq")
  @SequenceGenerator(
      name = "order_status_seq",
      sequenceName = "order_status_seq",
      schema = "main",
      allocationSize = 1)
  private Long id;

  @Column(name = "name", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private Instant createdAt;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
}
