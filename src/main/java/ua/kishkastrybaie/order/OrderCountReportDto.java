package ua.kishkastrybaie.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ua.kishkastrybaie.order.status.OrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCountReportDto extends RepresentationModel<OrderCountReportDto> {
  private Instant startDate;
  private Instant endDate;
  private Map<OrderStatus, Map<LocalDate, Integer>> details;
}
