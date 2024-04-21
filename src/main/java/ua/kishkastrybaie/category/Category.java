package ua.kishkastrybaie.category;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.kishkastrybaie.variation.Variation;

@Entity
@Table(name = "product_category", schema = "main")
@Getter
@Setter
@ToString
public class Category {
  @Id
  @GeneratedValue(generator = "category_seq")
  @SequenceGenerator(
      name = "category_seq",
      sequenceName = "category_seq",
      schema = "main",
      allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory")
  private List<Category> descendants = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "category_variation",
      schema = "main",
      joinColumns = {@JoinColumn(name = "product_category_id")},
      inverseJoinColumns = {@JoinColumn(name = "variation_id")})
  private List<Variation> variations = new ArrayList<>();

  @Column(name = "path", columnDefinition = "ltree", updatable = false, insertable = false)
  @Setter(AccessLevel.NONE)
  private String path;

  @PreRemove
  void preRemove() {
    if (!getDescendants().isEmpty()) {
      throw new CategoryHasDescendantsException(this.id);
    }
  }
}
