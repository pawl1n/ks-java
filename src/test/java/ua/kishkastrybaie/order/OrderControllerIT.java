package ua.kishkastrybaie.order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kishkastrybaie.authentication.AuthenticationRequest;
import ua.kishkastrybaie.category.CategoryRepository;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.item.OrderItemRequestDto;
import ua.kishkastrybaie.order.payment.type.PaymentType;
import ua.kishkastrybaie.order.shipping.method.ShippingMethod;
import ua.kishkastrybaie.order.status.OrderStatus;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.ProductRepository;
import ua.kishkastrybaie.product.item.ProductItem;
import ua.kishkastrybaie.product.item.ProductItemRepository;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private ProductItemRepository productItemRepository;
  @Autowired private OrderRepository orderRepository;
  private String token = "";
  private Long orderItemId = 1L;

  @BeforeAll
  public void setup() {
    RestAssured.port = springBootPort;

    User admin = new User();
    admin.setEmail("admin@admin");
    admin.setPassword(passwordEncoder.encode("admin"));
    admin.setFirstName("admin");
    admin.setPhoneNumber("+380501234567");
    admin.setRole(Role.ADMIN);
    admin = userRepository.save(admin);

    AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin@admin", "admin");

    token =
        given()
            .body(authenticationRequest)
            .contentType(ContentType.JSON)
            .when()
            .post("/api/auth/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("accessToken", notNullValue())
            .extract()
            .path("accessToken");

    Product product = new Product();
    product.setName("product");
    product.setDescription("description");
    product = productRepository.save(product);

    ProductItem productItem = new ProductItem();
    productItem.setProduct(product);
    productItem.setStock(10);
    productItem.setPrice(100.0);
    productItem.setSku("sku");
    productItem = productItemRepository.save(productItem);

    Order order = new Order();
    order.setStatus(OrderStatus.CREATED);
    order.setTotalPrice(100.0);
    order.setUserEmail(admin.getEmail());
    order.setShippingMethod(ShippingMethod.PICKUP);
    order.setCustomerFullName(admin.getFirstName());
    order.setAddress("address");
    order.setPaymentType(PaymentType.CASH);
    order.setPhoneNumber(admin.getPhoneNumber());

    OrderItem orderItem = new OrderItem();
    orderItem.setProductItem(productItem);
    orderItem.setQuantity(1);
    orderItem.setPrice(100.0);
    order.setItems(Set.of(orderItem));
    orderRepository.save(order);

    this.orderItemId = orderItem.getId();
  }

  @AfterEach
  public void afterEach() {}

  @AfterAll
  public void afterAll() {
    orderRepository.deleteAllInBatch();
    userRepository.deleteAllInBatch();
    productItemRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    categoryRepository.deleteAllInBatch();
  }

  @Test
  void shouldGetAllWhenAdmin() {
    given()
        .auth()
        .oauth2(token)
        .when()
        .get("/api/orders")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("_embedded.orders.size()", notNullValue(), "_embedded.orders[0]", notNullValue());
  }

  @Test
  void shouldNotGetAllWhenUnauthorized() {
    given().when().get("/api/orders").then().statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldGetOneWhenAdmin() {
    given()
        .auth()
        .oauth2(token)
        .when()
        .get("/api/orders/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "id",
            notNullValue(),
            "currentStatus",
            equalTo(OrderStatus.CREATED.toString()),
            "totalPrice",
            equalTo(100.0F),
            "shippingMethod",
            equalTo(ShippingMethod.PICKUP.toString()),
            "fullName",
            equalTo("admin"),
            "address",
            equalTo("address"),
            "paymentType",
            equalTo(PaymentType.CASH.toString()),
            "phoneNumber",
            equalTo("+380501234567"));
  }

  @Test
  void shouldSaveWhenAdmin() {
    OrderItemRequestDto orderItemRequestDto = new OrderItemRequestDto(orderItemId, 1);
    OrderRequestDto orderRequestDto =
        new OrderRequestDto(
            "380501234567",
            "admin@admin",
            "admin",
            PaymentType.CASH,
            "address",
            ShippingMethod.PICKUP,
            Set.of(orderItemRequestDto));

    given()
        .auth()
        .oauth2(token)
        .body(orderRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/orders")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("totalPrice", equalTo(100.0F));
  }
}
