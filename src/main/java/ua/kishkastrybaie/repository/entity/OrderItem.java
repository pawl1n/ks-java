package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item", schema = "main")
@Getter
@Setter
public class OrderItem {
  @Id
  @GeneratedValue(generator = "order_item_seq")
  @SequenceGenerator(
      name = "order_item_seq",
      sequenceName = "order_item_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @ManyToOne private Order order;

  @ManyToOne private ProductItem productItem;

  @Column private Integer quantity;

  @Column private Double price;
}
