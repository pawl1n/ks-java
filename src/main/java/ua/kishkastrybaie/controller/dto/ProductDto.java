package ua.kishkastrybaie.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Relation(itemRelation = "product", collectionRelation = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto extends RepresentationModel<ProductDto> {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String mainImage;
    private Set<ProductItemDto> variations;
}
