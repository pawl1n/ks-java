package ua.kishkastrybaie.category;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory")
  private Set<Category> descendants = new HashSet<>();

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
