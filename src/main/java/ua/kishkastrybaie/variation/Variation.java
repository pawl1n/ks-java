package ua.kishkastrybaie.variation;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "variation", schema = "main")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Variation {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "variation_seq")
  @SequenceGenerator(
      name = "variation_seq",
      schema = "main",
      sequenceName = "variation_seq",
      allocationSize = 1)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;
}
