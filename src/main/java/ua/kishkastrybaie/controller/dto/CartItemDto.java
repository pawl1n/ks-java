package ua.kishkastrybaie.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto extends RepresentationModel<CartItemDto> {
    private Long id;
    private Long productItemId;
    private String productName;
    private URL productImage;
    private Double productItemPrice;
    private Long quantity;
}
