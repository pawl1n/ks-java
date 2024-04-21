package ua.kishkastrybaie.order.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.order.OrderNotFoundException;
import ua.kishkastrybaie.order.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
  private final OrderItemRepository orderItemRepository;
  private final OrderRepository orderRepository;
  private final OrderItemModelAssembler orderItemModelAssembler;
  private final PagedResourcesAssembler<OrderItem> pagedResourcesAssembler;

  @Override
  public CollectionModel<OrderItemDto> findAllByOrderId(Long id, Pageable pageable) {
    if (!orderRepository.existsById(id)) {
      throw new OrderNotFoundException(id);
    }

    return pagedResourcesAssembler.toModel(
        orderItemRepository.findAllByOrderId(id, pageable), orderItemModelAssembler);
  }
}
