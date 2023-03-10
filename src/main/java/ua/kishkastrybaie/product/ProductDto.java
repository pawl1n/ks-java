package ua.kishkastrybaie.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Relation(itemRelation = "product", collectionRelation = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class ProductDto extends RepresentationModel<ProductDto> {
  private Long id;
  private String name;
  private String description;
  private String category;
  private URL mainImage;
}
