package ua.kishkastrybaie.variation;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.category.Category;

@Entity
@Table(name = "variation", schema = "main")
@Getter
@Setter
public class Variation implements Serializable {
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

  @ManyToMany
  @JoinTable(
          name = "category_variation",
          schema = "main",
          joinColumns = {@JoinColumn(name = "variation_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "product_category_id")
          })
  private Set<Category> categories = new HashSet<>();
}
