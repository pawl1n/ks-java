package ua.kishkastrybaie.product;

import jakarta.persistence.*;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.variation.ProductItem;

@Entity
@Table(name = "product", schema = "main")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {
  @Id
  @GeneratedValue(generator = "product_seq")
  @SequenceGenerator(
      name = "product_seq",
      sequenceName = "product_seq",
      schema = "main",
      allocationSize = 1)
  @Column(unique = true, nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column private String description;

  @OneToOne(cascade = CascadeType.MERGE)
  private Image mainImage;

  @ManyToOne private Category category;

  @OneToMany(mappedBy = "product")
  private Set<ProductItem> productItems;
}
