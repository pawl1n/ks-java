package ua.kishkastrybaie.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto extends RepresentationModel<OrderDto> {
    private Long id;
    private LocalDateTime date;
    private String shippingMethodName;
    private String orderStatusName;
    private Double totalPrice;
    private String address;
    private String paymentTypeName;
    private Set<OrderItemDto> orderItems;
}
