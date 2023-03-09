package ua.kishkastrybaie.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ua.kishkastrybaie.repository.entity.CartItem;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class CartDto extends RepresentationModel<CartDto> {
  private Long id;
  private Set<CartItem> items;
}
