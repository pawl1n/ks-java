package ua.kishkastrybaie.order.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ua.kishkastrybaie.variation.option.VariationOptionDto;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "orderItem", collectionRelation = "orderItems")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto extends RepresentationModel<OrderItemDto> {
  private Long id;
  private String sku;
  private Set<VariationOptionDto> variationOptions;
  private Integer quantity;
  private Double price;
  private String productName;
}
