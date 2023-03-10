package ua.kishkastrybaie.variation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ua.kishkastrybaie.category.CategoryDto;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariationDto extends RepresentationModel<VariationDto> {
  private Long id;
  private CategoryDto categoryDto;
  private String name;
}
