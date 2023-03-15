package ua.kishkastrybaie.variation.option;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.variation.Variation;

@Entity
@Table(name = "variation_option", schema = "main")
@Getter
@Setter
@IdClass(VariationOptionId.class)
public class VariationOption {

  @ManyToOne @PrimaryKeyJoinColumn private Variation variation;

  @Id
  @Column(name = "`value`")
  private String value;
}
