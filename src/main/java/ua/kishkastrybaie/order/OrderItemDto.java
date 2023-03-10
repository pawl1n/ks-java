package ua.kishkastrybaie.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto extends RepresentationModel<OrderItemDto> {
  private Long id;
  private String productName;
  private URL productImage;
  private Double price;
  private Integer quantity;
}
