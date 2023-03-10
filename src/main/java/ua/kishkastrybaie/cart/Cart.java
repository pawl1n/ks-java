package ua.kishkastrybaie.cart;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.user.User;

@Entity
@Table(name = "user_cart", schema = "main")
@Getter
@Setter
public class Cart {
  @Id
  @GeneratedValue(generator = "user_cart_seq")
  @SequenceGenerator(
      name = "user_cart_seq",
      sequenceName = "user_cart_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "cart")
  private Set<CartItem> items;
}
