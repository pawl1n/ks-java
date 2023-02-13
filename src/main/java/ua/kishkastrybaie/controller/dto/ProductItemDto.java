package ua.kishkastrybaie.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductItemDto extends RepresentationModel<ProductItemDto> {
    private Long id;
    private ProductDto productDto;
    private String sku;
    private Double price;
    private Integer stock;
    private Set<VariationOptionDto> variations;
}
