package ua.kishkastrybaie.order;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static ua.kishkastrybaie.order.status.OrderStatus.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.item.OrderItemQuantityOutOfBoundsException;
import ua.kishkastrybaie.order.item.OrderItemRepository;
import ua.kishkastrybaie.order.item.OrderItemRequestDto;
import ua.kishkastrybaie.order.status.OrderStatus;
import ua.kishkastrybaie.product.item.ProductItem;
import ua.kishkastrybaie.product.item.ProductItemNotFoundException;
import ua.kishkastrybaie.product.item.ProductItemRepository;
import ua.kishkastrybaie.user.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final PagedResourcesAssembler<Order> pagedResourcesAssembler;
  private final OrderModelAssembler orderModelAssembler;
  private final ProductItemRepository productItemRepository;
  private final OrderItemRepository orderItemRepository;

  @Override
  @Transactional
  public CollectionModel<OrderDto> findAll(Pageable pageable) {
    return pagedResourcesAssembler.toModel(orderRepository.findAll(pageable), orderModelAssembler);
  }

  @Override
  @Transactional
  public CollectionModel<OrderDto> findAllByUser(Pageable pageable, User user) {
    Page<Order> orders = orderRepository.findAllByUserEmail(user.getEmail(), pageable);
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
    order.setUserEmail(orderRequest.userEmail().toLowerCase());

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
                  o.setUserEmail(orderRequest.userEmail().toLowerCase());

                  processProductItemsReturning(o.getItems());
                  o.setItems(getItems(orderRequest.items()));
                  processProductItemsSelling(o.getItems());

                  o.setTotalPrice(calculateTotalPrice(o));

                  changeStatus(o, orderRequest.status());

                  return orderRepository.save(o);
                })
            .orElseThrow(() -> new OrderNotFoundException(id));

    return orderModelAssembler.toModel(orderRepository.save(order));
  }

  @Override
  @Transactional
  public OrderCountReportDto getReport(Instant startDate, Instant endDate) {
    List<IOrderCountReport> report =
        orderRepository.countByStatusHistoryCreatedAtBetweenAndStatusHistoryStatusIs(
            startDate, endDate);

    if (report.isEmpty()) {
      return null;
    }

      Map<OrderStatus, Map<LocalDate, Integer>> reportByDate =
          report.stream()
              .collect(
                  groupingBy(
                      IOrderCountReport::getStatus,
                      groupingBy(
                          IOrderCountReport::getDate,
                          summingInt(IOrderCountReport::getCount))));

    OrderCountReportDto countReportDto = new OrderCountReportDto();
    countReportDto.setStartDate(startDate);
    countReportDto.setEndDate(endDate);
    countReportDto.setDetails(reportByDate);

    return countReportDto;
  }

  private void changeStatus(Order order, OrderStatus status) {
    if (!order.getStatus().equals(status)) {
      if (status.equals(CANCELED)) {
        processProductItemsReturning(order.getItems());
      } else if (order.getStatus().equals(CANCELED)) {
        processProductItemsSelling(order.getItems());
      }

      order.setStatus(status);
    }
  }

  private List<OrderItem> getItems(Set<OrderItemRequestDto> items) {
    return items.stream().map(this::mapToOrderItem).toList();
  }

  private OrderItem mapToOrderItem(OrderItemRequestDto orderItemRequestDto) {
    ProductItem productItem =
        productItemRepository
            .findById(orderItemRequestDto.productItem())
            .orElseThrow(() -> new ProductItemNotFoundException(orderItemRequestDto.productItem()));

    OrderItem orderItem = new OrderItem();
    orderItem.setProductItem(productItem);
    orderItem.setQuantity(orderItemRequestDto.quantity());
    orderItem.setPrice(productItem.getProduct().getPrice() * orderItemRequestDto.quantity());

    return orderItem;
  }

  private void processProductItemsReturning(Iterable<OrderItem> items) {
    items.forEach(
        item -> {
          ProductItem productItem = item.getProductItem();
          productItem.setStock(productItem.getStock() + item.getQuantity());

          productItemRepository.save(productItem);
        });
  }

  private void processProductItemsSelling(Iterable<OrderItem> items) {
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
