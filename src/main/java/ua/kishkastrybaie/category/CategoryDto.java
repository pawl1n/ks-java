package ua.kishkastrybaie.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "category", collectionRelation = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto extends RepresentationModel<CategoryDto> {
  private final Long id;
  private final String name;
  private final String parentCategory;
}
