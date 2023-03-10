package ua.kishkastrybaie.variation;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.product.Product;

@Entity
@Table(name = "product_item", schema = "main")
@Getter
@Setter
public class ProductItem {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_item_seq")
  @SequenceGenerator(
      name = "product_item_seq",
      schema = "main",
      sequenceName = "product_item_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne private Product product;

  @Column private String sku;

  @Column private Double price;

  @Column private Integer stock;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "product_configuration",
      joinColumns = {@JoinColumn(name = "product_item_id")},
      inverseJoinColumns = {@JoinColumn(name = "variation_option_id")})
  private Set<VariationOption> variationOption;
}
