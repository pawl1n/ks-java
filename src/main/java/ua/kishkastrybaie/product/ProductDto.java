package ua.kishkastrybaie.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ua.kishkastrybaie.category.CategoryDto;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "product", collectionRelation = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto extends RepresentationModel<ProductDto> {
  private final Long id;
  private final String name;
  private final String description;
  private final CategoryDto category;
  private final URL mainImage;
}
