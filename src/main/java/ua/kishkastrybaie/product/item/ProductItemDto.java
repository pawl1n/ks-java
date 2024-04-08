package ua.kishkastrybaie.product.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ua.kishkastrybaie.variation.option.VariationOptionDto;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(itemRelation = "productItem", collectionRelation = "productItems")
public class ProductItemDto extends RepresentationModel<ProductItemDto> {
  private Long id;
  private String productName;
  private String description;
  private String sku;
  private Double price;
  private Integer stock;
  private Set<VariationOptionDto> variationOptions;
}
