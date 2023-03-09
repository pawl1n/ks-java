package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_cart_item", schema = "main")
@Getter
@Setter
public class CartItem {
  @Id
  @GeneratedValue(generator = "user_cart_item_seq")
  @SequenceGenerator(
      name = "user_cart_item_seq",
      sequenceName = "user_cart_item_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @ManyToOne private Cart cart;

  @ManyToOne private ProductItem productItem;

  @Column(name = "quantity")
  private Integer quantity;
}
