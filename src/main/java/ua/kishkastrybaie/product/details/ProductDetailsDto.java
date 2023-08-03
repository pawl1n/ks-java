package ua.kishkastrybaie.product.details;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ua.kishkastrybaie.product.ProductDto;
import ua.kishkastrybaie.product.item.ProductItemDto;
import ua.kishkastrybaie.shared.BreadCrumb;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "product", collectionRelation = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailsDto extends RepresentationModel<ProductDetailsDto> {
  private final ProductDto product;
  private final Set<ProductItemDto> variations;
  private final BreadCrumb breadcrumbs;
}
