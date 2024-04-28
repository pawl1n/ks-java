package ua.kishkastrybaie.order;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.payment.type.PaymentType;
import ua.kishkastrybaie.order.shipping.method.ShippingMethod;
import ua.kishkastrybaie.order.status.OrderStatus;

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

  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;

  private String phoneNumber;

  private String customerFullName;

  @Enumerated(EnumType.STRING)
  private ShippingMethod shippingMethod;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @Setter(AccessLevel.NONE)
  private List<OrderItem> items = new ArrayList<>();

  @CreationTimestamp
  @Setter(AccessLevel.NONE)
  private Instant createdAt;

  /**
   * Set items for order and set order for order items
   *
   * @param orderItems - order items
   */
  public void setItems(List<OrderItem> orderItems) {
    List<OrderItem> mappedOrderItems = orderItems.stream().map(
        item -> {
          Optional<OrderItem> orderItem =
              this.items.stream()
                  .filter(
                      value -> value.getProductItem().getId().equals(item.getProductItem().getId()))
                  .findFirst();

          if (orderItem.isPresent()) {
            orderItem.get().setQuantity(item.getQuantity());
            return orderItem.get();
          } else {
            item.setOrder(this);
            return item;
          }
        }).toList();
    this.items = new ArrayList<>(mappedOrderItems);
  }
}
