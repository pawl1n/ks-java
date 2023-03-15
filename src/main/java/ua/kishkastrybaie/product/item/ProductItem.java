package ua.kishkastrybaie.product.item;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.variation.option.VariationOption;

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

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "product_configuration",
      schema = "main",
      joinColumns = {@JoinColumn(name = "product_item_id")},
      inverseJoinColumns = {
        @JoinColumn(name = "variation_value"),
        @JoinColumn(name = "variation_id")
      })
  private Set<VariationOption> variationOptions;
}
