package ua.kishkastrybaie.order;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import ua.kishkastrybaie.user.User;

public interface OrderService {
  CollectionModel<OrderDto> findAll(Pageable pageable);

  CollectionModel<OrderDto> findAllByUser(Pageable pageable, User user);

  OrderDto findById(Long id);

  OrderDto save(OrderRequestDto orderRequest);

  OrderDto replace(Long id, OrderRequestDto orderRequest);
}
