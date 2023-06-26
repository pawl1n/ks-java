package ua.kishkastrybaie.order;

import static ua.kishkastrybaie.order.status.OrderStatus.*;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.item.OrderItemQuantityOutOfBoundsException;
import ua.kishkastrybaie.order.item.OrderItemRequestDto;
import ua.kishkastrybaie.order.status.OrderStatus;
import ua.kishkastrybaie.product.item.ProductItem;
import ua.kishkastrybaie.product.item.ProductItemNotFoundException;
import ua.kishkastrybaie.product.item.ProductItemRepository;
import ua.kishkastrybaie.shared.AuthorizationService;
import ua.kishkastrybaie.user.User;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final PagedResourcesAssembler<Order> pagedResourcesAssembler;
  private final OrderModelAssembler orderModelAssembler;
  private final AuthorizationService authorizationService;
  private final ProductItemRepository productItemRepository;

  @Override
  @Transactional
  public CollectionModel<OrderDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(orderRepository.findAll(pageable), orderModelAssembler);
  }

  @Override
  @Transactional
  public CollectionModel<OrderDto> findAllByUser(Pageable pageable, User user) {
    Page<Order> orders =
        orderRepository.findAllByUserEmail(
            authorizationService.getAuthenticatedUser().getEmail(), pageable);

    return pagedResourcesAssembler.toModel(orders, orderModelAssembler);
  }

  @Override
  @Transactional
  public OrderDto findById(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    return orderModelAssembler.toModel(order);
  }

  @Override
  @Transactional
  public synchronized OrderDto save(OrderRequestDto orderRequest) {
    Order order = new Order();
    order.setAddress(orderRequest.address());
    order.setCustomerFullName(orderRequest.customerFullName());
    order.setPaymentType(orderRequest.paymentType());
    order.setPhoneNumber(orderRequest.phoneNumber());
    order.setShippingMethod(orderRequest.shippingMethod());
    order.setUserEmail(orderRequest.email().toLowerCase());

    order.setItems(getItems(orderRequest.items()));
    order.setTotalPrice(calculateTotalPrice(order));
    order.setStatus(CREATED);

    processProductItemsSelling(order.getItems());

    return orderModelAssembler.toModel(orderRepository.save(order));
  }

  @Override
  @Transactional
  public synchronized OrderDto replace(Long id, OrderRequestDto orderRequest) {
    Order order =
        orderRepository
            .findById(id)
            .map(
                o -> {
                  o.setPaymentType(orderRequest.paymentType());
                  o.setPhoneNumber(orderRequest.phoneNumber());
                  o.setAddress(orderRequest.address());
                  o.setShippingMethod(orderRequest.shippingMethod());
                  o.setCustomerFullName(orderRequest.customerFullName());
                  o.setUserEmail(orderRequest.email().toLowerCase());

                  processProductItemsReturning(o.getItems());
                  o.setItems(getItems(orderRequest.items()));
                  processProductItemsSelling(o.getItems());

                  o.setTotalPrice(calculateTotalPrice(o));
                  return orderRepository.save(o);
                })
            .orElseThrow(() -> new OrderNotFoundException(id));

    return orderModelAssembler.toModel(orderRepository.save(order));
  }

  @Override
  @Transactional
  public OrderDto changeStatus(Long id, OrderStatus status) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (!order.getStatus().equals(status)) {
      if (status.equals(CANCELED)) {
        processProductItemsReturning(order.getItems());
      } else if (order.getStatus().equals(CANCELED)) {
        processProductItemsSelling(order.getItems());
      }

      order.setStatus(status);
    }

    return orderModelAssembler.toModel(orderRepository.save(order));
  }

  private Set<OrderItem> getItems(Set<OrderItemRequestDto> items) {
    return items.stream().map(this::mapToOrderItem).collect(Collectors.toSet());
  }

  private OrderItem mapToOrderItem(OrderItemRequestDto orderItemRequestDto) {
    ProductItem productItem =
        productItemRepository
            .findById(orderItemRequestDto.productItem())
            .orElseThrow(() -> new ProductItemNotFoundException(orderItemRequestDto.productItem()));

    OrderItem orderItem = new OrderItem();
    orderItem.setProductItem(productItem);
    orderItem.setQuantity(orderItemRequestDto.quantity());
    orderItem.setPrice(productItem.getPrice() * orderItemRequestDto.quantity());

    return orderItem;
  }

  private void processProductItemsReturning(Set<OrderItem> items) {
    items.forEach(
        item -> {
          ProductItem productItem = item.getProductItem();
          productItem.setStock(productItem.getStock() + item.getQuantity());

          productItemRepository.save(productItem);
        });
  }

  private void processProductItemsSelling(Set<OrderItem> items) {
    items.forEach(
        item -> {
          ProductItem productItem = item.getProductItem();

          if (productItem.getStock() - item.getQuantity() < 0) {
            throw new OrderItemQuantityOutOfBoundsException(
                productItem.getId(), productItem.getStock());
          }

          productItem.setStock(productItem.getStock() - item.getQuantity());

          productItemRepository.save(productItem);
        });
  }

  private Double calculateTotalPrice(Order order) {
    return order.getItems().stream().mapToDouble(OrderItem::getPrice).sum();
  }
}
