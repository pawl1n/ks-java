package ua.kishkastrybaie.order;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.order.status.OrderStatus;
import ua.kishkastrybaie.shared.StatisticsDto;
import ua.kishkastrybaie.user.User;

import java.time.Instant;

public interface OrderService {
  CollectionModel<OrderDto> findAll(Pageable pageable);

  CollectionModel<OrderDto> findAllByUser(Pageable pageable, User user);

  OrderDto findById(Long id);

  OrderDto save(OrderRequestDto orderRequest);

  OrderDto replace(Long id, OrderRequestDto orderRequest);

  OrderCountReportDto getReport(Instant startDate, Instant endDate);

  StatisticsDto getCountStatistics(OrderStatus orderStatus, Instant startDate, Instant endDate);
}
