package ua.kishkastrybaie.variation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariationOptionDto extends RepresentationModel<VariationOptionDto> {
  private Long id;
  private VariationDto variationDto;
  private String value;
}
