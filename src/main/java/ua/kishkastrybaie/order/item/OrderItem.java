package ua.kishkastrybaie.order.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.kishkastrybaie.order.Order;
import ua.kishkastrybaie.product.item.ProductItem;

@Entity
@Table(name = "order_item", schema = "main")
@Getter
@Setter
@ToString
public class OrderItem {
  @Id
  @GeneratedValue(generator = "order_item_seq")
  @SequenceGenerator(
      name = "order_item_seq",
      sequenceName = "order_item_seq",
      schema = "main",
      allocationSize = 1)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_item_id")
  private ProductItem productItem;

  private Integer quantity;
  private Double price;
}
