package ua.kishkastrybaie.order;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.user.User;

@Entity
@Table(name = "`order`", schema = "main")
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue(generator = "order_seq")
  @SequenceGenerator(
      name = "order_seq",
      sequenceName = "order_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @ManyToOne private User user;

  @Column private LocalDateTime date;

  @ManyToOne private ShippingMethod shippingMethod;

  @ManyToOne private OrderStatus orderStatus;

  @Column private Double totalPrice;

  @Column private String address;

  @ManyToOne private PaymentType paymentType;

  @OneToMany(mappedBy = "order")
  private Set<OrderItem> orderItems;
}
