package ua.kishkastrybaie.variation;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.category.Category;

@Entity
@Table(name = "variation", schema = "main")
@Getter
@Setter
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

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "variation")
  private Set<VariationOption> variationOptions;
}
