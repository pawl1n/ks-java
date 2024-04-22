package ua.kishkastrybaie.product;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.product.item.ProductItem;

@Entity
@Table(name = "product", schema = "main")
@Getter
@Setter
@Indexed
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

  @FullTextField
  @Column(nullable = false)
  private String name;

  @FullTextField
  @Column private String description;

  @Column private String slug;

  @FullTextField
  @Column private String sku;

  @Column private Double price;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Image mainImage;

  @ManyToOne private Category category;
}
