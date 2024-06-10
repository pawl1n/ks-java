package ua.kishkastrybaie.order;

import lombok.Data;
import ua.kishkastrybaie.order.status.OrderStatus;

import java.time.LocalDate;

@Data
public class OrderCountReport implements IOrderCountReport {
    private LocalDate date;
    private Integer count;
    private OrderStatus status;
}
