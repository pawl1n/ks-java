package ua.kishkastrybaie.variation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "variation", collectionRelation = "variations")
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class VariationDto extends RepresentationModel<VariationDto> {
  private Long id;
  private String name;
}
