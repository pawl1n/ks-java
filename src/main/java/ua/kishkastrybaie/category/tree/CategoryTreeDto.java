package ua.kishkastrybaie.category.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "category", collectionRelation = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryTreeDto extends RepresentationModel<CategoryTreeDto> {
  private final Long id;
  private final String name;
  private final String path;
  private final List<CategoryTreeDto> descendants;
}
