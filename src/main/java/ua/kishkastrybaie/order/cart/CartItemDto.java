package ua.kishkastrybaie.order.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class CartItemDto extends RepresentationModel<CartItemDto> {
  private Long id;
  private Long productItemId;
  private String productName;
  private URL productImage;
  private Double productItemPrice;
  private Long quantity;
}
