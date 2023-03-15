package ua.kishkastrybaie.variation.option;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Relation(itemRelation = "option", collectionRelation = "options")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariationOptionDto extends RepresentationModel<VariationOptionDto> {
  private Long variationId;
  private String value;
}
