package ua.kishkastrybaie.order.item;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

public interface OrderItemService {
  CollectionModel<OrderItemDto> findAllByOrderId(Long id, Pageable pageable);
}
