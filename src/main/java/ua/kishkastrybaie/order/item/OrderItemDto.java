package ua.kishkastrybaie.order.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ua.kishkastrybaie.product.item.ProductItemDto;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "orderItem", collectionRelation = "orderItems")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto extends RepresentationModel<OrderItemDto> {
  private Long id;
  private String sku;
  private ProductItemDto productItem;
  private Integer quantity;
  private Double price;
  private String productName;
}
