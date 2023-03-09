package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "variation_option", schema = "main")
@Getter
@Setter
public class VariationOption {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "variation_option_seq")
  @SequenceGenerator(
      name = "variation_option_seq",
      schema = "main",
      sequenceName = "variation_option_seq",
      allocationSize = 1)
  private Long id;

  @ManyToOne private Variation variation;

  @Column(name = "`value`")
  private String value;
}
