package ua.kishkastrybaie.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ua.kishkastrybaie.order.payment.type.PaymentType;
import ua.kishkastrybaie.order.status.OrderStatus;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "order", collectionRelation = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto extends RepresentationModel<OrderDto> {
    private Long id;
    private String userEmail;
    private Double totalPrice;
    private String address;
    private OrderStatus currentStatus;
    private PaymentType paymentType;
}
