package ua.kishkastrybaie.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipping_method", schema = "main")
@Getter
@Setter
public class ShippingMethod {
  @Id
  @GeneratedValue(generator = "shipping_method_seq")
  @SequenceGenerator(
      name = "shipping_method_seq",
      sequenceName = "shipping_method_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @Column private String name;
}
