package ua.kishkastrybaie.order;

import jakarta.persistence.*;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.payment.type.PaymentType;
import ua.kishkastrybaie.order.shipping.method.ShippingMethod;
import ua.kishkastrybaie.order.status.OrderStatus;
import ua.kishkastrybaie.order.status.OrderStatusHistory;

@Entity
@Table(name = "`order`", schema = "main")
@Getter
@Setter
@EqualsAndHashCode
public class Order {
  @Id
  @GeneratedValue(generator = "order_seq")
  @SequenceGenerator(
      name = "order_seq",
      sequenceName = "order_seq",
      schema = "main",
      allocationSize = 1)
  private Long id;

  private String userEmail;

  private Double totalPrice;

  private String address;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private Set<OrderStatusHistory> statusHistory;

  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;

  private String phoneNumber;

  private String customerFullName;

  @Enumerated(EnumType.STRING)
  private ShippingMethod shippingMethod;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private Set<OrderItem> items;
}
