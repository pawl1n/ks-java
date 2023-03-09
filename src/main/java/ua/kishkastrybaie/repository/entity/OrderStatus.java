package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_status", schema = "main")
@Getter
@Setter
public class OrderStatus {
  @Id
  @GeneratedValue(generator = "order_status_seq")
  @SequenceGenerator(
      name = "order_status_seq",
      sequenceName = "order_status_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", updatable = false)
  private Long id;

  @Column private String name;
}
