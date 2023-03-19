package ua.kishkastrybaie.product;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.product.item.ProductItem;

@Entity
@Table(name = "product", schema = "main")
@Getter
@Setter
public class Product {
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private final Set<ProductItem> productItems = new HashSet<>();
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
}
