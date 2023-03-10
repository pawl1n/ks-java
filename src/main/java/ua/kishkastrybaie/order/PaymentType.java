package ua.kishkastrybaie.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment_type", schema = "main")
@Getter
@Setter
public class PaymentType {
  @Id
  @GeneratedValue(generator = "payment_type_seq")
  @SequenceGenerator(
      name = "payment_type_seq",
      sequenceName = "payment_type_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @Column private String name;

  @Column private String description;
}
