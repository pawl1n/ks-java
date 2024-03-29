package ua.kishkastrybaie.order;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import ua.kishkastrybaie.category.Category;
import ua.kishkastrybaie.image.Image;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.item.OrderItemDto;
import ua.kishkastrybaie.order.item.OrderItemQuantityOutOfBoundsException;
import ua.kishkastrybaie.order.item.OrderItemRequestDto;
import ua.kishkastrybaie.order.payment.type.PaymentType;
import ua.kishkastrybaie.order.shipping.method.ShippingMethod;
import ua.kishkastrybaie.order.status.OrderStatus;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.item.ProductItem;
import ua.kishkastrybaie.product.item.ProductItemNotFoundException;
import ua.kishkastrybaie.product.item.ProductItemRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
  private Order order;
  private OrderDto orderDto1;
  private OrderRequestDto orderRequestDto;
  private ProductItem productItem1;
  private ProductItem productItem2;
  @Mock private OrderRepository orderRepository;
  @Mock private PagedResourcesAssembler<Order> pagedResourcesAssembler;
  @Mock private OrderModelAssembler orderModelAssembler;
  @Mock private ProductItemRepository productItemRepository;
  @InjectMocks private OrderServiceImpl orderService;

  @BeforeEach
  void setUp() {
    Category category = new Category();
    category.setId(1L);
    category.setName("Category 1");

    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Product 1");
    product1.setDescription("Description 1");
    product1.setCategory(category);
    product1.setMainImage(new Image());

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Product 2");

    productItem1 = new ProductItem();
    productItem1.setId(1L);
    productItem1.setProduct(product1);
    productItem1.setPrice(10.0);
    productItem1.setStock(10);

    productItem2 = new ProductItem();
    productItem2.setId(2L);
    productItem2.setProduct(product2);
    productItem2.setPrice(20.0);
    productItem2.setStock(20);

    OrderItem orderItem1 = new OrderItem();
    orderItem1.setId(1L);
    orderItem1.setProductItem(productItem1);
    orderItem1.setQuantity(1);
    orderItem1.setOrder(order);
    orderItem1.setPrice(10.0);

    OrderItem orderItem2 = new OrderItem();
    orderItem2.setId(2L);
    orderItem2.setProductItem(productItem2);
    orderItem2.setQuantity(2);
    orderItem2.setOrder(order);
    orderItem2.setPrice(40.0);

    OrderItemDto orderItemDto1 = new OrderItemDto(1L, null, null, 1, 10.0, "Product 1");
    OrderItemDto orderItemDto2 = new OrderItemDto(2L, null, null, 2, 40.0, "Product 2");

    order = new Order();
    order.setId(1L);
    order.setItems(new HashSet<>(Arrays.asList(orderItem1, orderItem2)));
    order.setTotalPrice(50.0);
    order.setStatus(OrderStatus.CREATED);
    order.setUserEmail("user@example.com");
    order.setShippingMethod(ShippingMethod.PICKUP);
    order.setPaymentType(PaymentType.CASH);
    order.setAddress("test address");
    order.setCustomerFullName("User Test");
    order.setPhoneNumber("380111111111");

    orderDto1 =
        new OrderDto(
            1L,
            "user@example.com",
            50.0,
            "test address",
            OrderStatus.CREATED,
            PaymentType.CASH,
            ShippingMethod.PICKUP,
            "User Test",
            "380111111111",
            Set.of(orderItemDto1, orderItemDto2));

    OrderItemRequestDto orderItemRequestDto1 = new OrderItemRequestDto(1L, 1);
    OrderItemRequestDto orderItemRequestDto2 = new OrderItemRequestDto(2L, 2);

    Set<OrderItemRequestDto> orderItemRequestDtoSet =
        new HashSet<>(List.of(orderItemRequestDto1, orderItemRequestDto2));

    orderRequestDto =
        new OrderRequestDto(
            "380111111111",
            "user@example.com",
            "User Test",
            PaymentType.CASH,
            "test address",
            ShippingMethod.PICKUP,
            orderItemRequestDtoSet);
  }

  @Test
  void shouldFindAllWhenAdmin() {
    // given
    Page<Order> orders = new PageImpl<>(List.of(order));
    PagedModel<OrderDto> orderDtoPagedModel =
        PagedModel.of(List.of(orderDto1), new PagedModel.PageMetadata(5, 0, 2));
    given(orderRepository.findAll(PageRequest.ofSize(5))).willReturn(orders);
    given(pagedResourcesAssembler.toModel(orders, orderModelAssembler))
        .willReturn(orderDtoPagedModel);

    // when
    CollectionModel<OrderDto> ordersDto = orderService.findAll(PageRequest.ofSize(5));

    // then
    then(ordersDto).hasSize(1).usingRecursiveComparison().isEqualTo(orderDtoPagedModel);
  }
//
//  @Test
//  void shouldFindAllWhenUser() {
//    // given
//    User user = new User();
//    user.setEmail("user@example.com");
//
//    Page<Order> orders = new PageImpl<>(List.of(order));
//    PagedModel<OrderDto> orderDtoPagedModel =
//        PagedModel.of(List.of(orderDto1), new PagedModel.PageMetadata(5, 0, 2));
//    given(authorizationService.isAdmin()).willReturn(false);
//    given(authorizationService.getAuthenticatedUser()).willReturn(user);
//    given(orderRepository.findAllByUserEmail("user@example.com", PageRequest.ofSize(5)))
//        .willReturn(orders);
//    given(pagedResourcesAssembler.toModel(orders, orderModelAssembler))
//        .willReturn(orderDtoPagedModel);
//
//    // when
//    CollectionModel<OrderDto> ordersDto = orderService.findAll(PageRequest.ofSize(5));
//
//    // then
//    then(ordersDto).hasSize(1).usingRecursiveComparison().isEqualTo(orderDtoPagedModel);
//  }

  @Test
  void shouldFindById() {
    // given
    given(orderRepository.findById(1L)).willReturn(Optional.of(order));
    given(orderModelAssembler.toModel(order)).willReturn(orderDto1);

    // when
    OrderDto orderDto = orderService.findById(1L);

    // then
    then(orderDto).usingRecursiveComparison().isEqualTo(orderDto1);
  }

  @Test
  void shouldNotFindById() {
    // given

    // when
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> orderService.findById(1L)).isInstanceOf(OrderNotFoundException.class);
    verifyNoInteractions(orderModelAssembler);
  }

  @Test
  void shouldSave() {
    // given
    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

    given(productItemRepository.findById(1L)).willReturn(Optional.of(productItem1));
    given(productItemRepository.findById(2L)).willReturn(Optional.of(productItem2));
    given(orderRepository.save(captor.capture())).willReturn(order);
    given(orderModelAssembler.toModel(order)).willReturn(orderDto1);

    // when
    OrderDto result = orderService.save(orderRequestDto);

    // then
    then(captor.getValue())
        .usingRecursiveComparison()
        .ignoringFields("id", "items")
        .isEqualTo(order);
    then(result).usingRecursiveComparison().isEqualTo(orderDto1);
  }

  @Test
  void shouldNotSaveWhenInvalidProductItemId() {
    // given
    given(productItemRepository.findById(1L)).willReturn(Optional.of(productItem1));

    // when
    when(productItemRepository.findById(2L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> orderService.save(orderRequestDto))
        .isInstanceOf(ProductItemNotFoundException.class);
  }

  @Test
  void shouldNotSaveWhenQuantityTooBig() {
    // given
    given(productItemRepository.findById(1L)).willReturn(Optional.of(productItem1));

    OrderItemRequestDto orderItemRequestDto1 = new OrderItemRequestDto(1L, 1);
    OrderItemRequestDto orderItemRequestDto2 = new OrderItemRequestDto(2L, 21);

    Set<OrderItemRequestDto> orderItemRequestDtoSet =
        new HashSet<>(List.of(orderItemRequestDto1, orderItemRequestDto2));

    orderRequestDto =
        new OrderRequestDto(
            "380111111111",
            "user@example.com",
            "User Test",
            PaymentType.CASH,
            "test address",
            ShippingMethod.PICKUP,
            orderItemRequestDtoSet);

    // when
    when(productItemRepository.findById(2L)).thenReturn(Optional.of(productItem2)); // quantity = 20

    // then
    thenThrownBy(() -> orderService.save(orderRequestDto))
        .isInstanceOf(OrderItemQuantityOutOfBoundsException.class);
  }

  @Test
  void shouldReplace() {
    // given
    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

    given(orderRepository.findById(1L)).willReturn(Optional.of(order));
    given(productItemRepository.findById(1L)).willReturn(Optional.of(productItem1));
    given(productItemRepository.findById(2L)).willReturn(Optional.of(productItem2));
    given(orderRepository.save(captor.capture())).willReturn(order);
    given(orderModelAssembler.toModel(order)).willReturn(orderDto1);

    // when
    OrderDto result = orderService.replace(1L, orderRequestDto);

    // then
    then(captor.getValue()).usingRecursiveComparison().isEqualTo(order);
    then(result).usingRecursiveComparison().isEqualTo(orderDto1);
  }

  @Test
  void shouldNotReplaceWhenInvalidProductItemId() {
    // given
    given(orderRepository.findById(1L)).willReturn(Optional.of(order));
    given(productItemRepository.findById(1L)).willReturn(Optional.of(productItem1));

    // when
    when(productItemRepository.findById(2L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> orderService.replace(1L, orderRequestDto))
        .isInstanceOf(ProductItemNotFoundException.class);
  }

  @Test
  void shouldNotReplaceWhenInvalidOrderId() {
    // given

    // when
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> orderService.replace(1L, orderRequestDto))
        .isInstanceOf(OrderNotFoundException.class);
  }

  @Test
  void shouldNotReplaceWhenQuantityTooBig() {
    // given
    given(orderRepository.findById(1L)).willReturn(Optional.of(order));
    given(productItemRepository.findById(1L)).willReturn(Optional.of(productItem1));

    OrderItemRequestDto orderItemRequestDto1 = new OrderItemRequestDto(1L, 1);
    OrderItemRequestDto orderItemRequestDto2 = new OrderItemRequestDto(2L, 100);

    Set<OrderItemRequestDto> orderItemRequestDtoSet =
        new HashSet<>(List.of(orderItemRequestDto1, orderItemRequestDto2));

    orderRequestDto =
        new OrderRequestDto(
            "380111111111",
            "user@example.com",
            "User Test",
            PaymentType.CASH,
            "test address",
            ShippingMethod.PICKUP,
            orderItemRequestDtoSet);

    // when
    when(productItemRepository.findById(2L)).thenReturn(Optional.of(productItem2)); // quantity = 20

    // then
    thenThrownBy(() -> orderService.replace(1L, orderRequestDto))
        .isInstanceOf(OrderItemQuantityOutOfBoundsException.class);
  }

  @Test
  void shouldChangeStatus() {
    // given
    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

    given(orderRepository.findById(1L)).willReturn(Optional.of(order));
    given(orderRepository.save(captor.capture())).willReturn(order);
    given(orderModelAssembler.toModel(order)).willReturn(orderDto1);

    // when
    OrderDto result = orderService.changeStatus(1L, OrderStatus.COMPLETED);

    // then
    then(captor.getValue()).usingRecursiveComparison().isEqualTo(order);
    then(result).usingRecursiveComparison().isEqualTo(orderDto1);
  }

  @Test
  void shouldNotChangeStatusWhenInvalidOrderId() {
    // given

    // when
    given(orderRepository.findById(1L)).willReturn(Optional.empty());

    // then
    thenThrownBy(() -> orderService.changeStatus(1L, OrderStatus.COMPLETED))
        .isInstanceOf(OrderNotFoundException.class);
  }

  @Test
  void shouldNotChangeStatusWhenSameStatus() {
    // given
    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

    given(orderRepository.findById(1L)).willReturn(Optional.of(order));
    given(orderRepository.save(captor.capture())).willReturn(order);
    given(orderModelAssembler.toModel(order)).willReturn(orderDto1);

    // when
    OrderDto result = orderService.changeStatus(1L, order.getStatus());

    // then
    then(captor.getValue()).usingRecursiveComparison().isEqualTo(order);
    then(result).usingRecursiveComparison().isEqualTo(orderDto1);
  }
}
