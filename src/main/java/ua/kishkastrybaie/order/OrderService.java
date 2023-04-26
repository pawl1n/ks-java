package ua.kishkastrybaie.order;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.order.status.OrderStatus;

public interface OrderService {
  CollectionModel<OrderDto> findAll(Pageable pageable);

  OrderDto findById(Long id);

  OrderDto save(OrderRequestDto orderRequest);

  OrderDto replace(Long id, OrderRequestDto orderRequest);

  OrderDto changeStatus(Long id, OrderStatus status);
}
