package ua.kishkastrybaie.product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import ua.kishkastrybaie.authentication.AuthenticationRequest;
import ua.kishkastrybaie.category.CategoryRepository;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ProductRepository productRepository;
  private String token = "";

  @BeforeAll
  public void setup() {
    RestAssured.port = springBootPort;

    User user = new User();
    user.setEmail("admin@admin");
    user.setPassword(passwordEncoder.encode("admin"));
    user.setFirstName("admin");
    user.setPhoneNumber("+380501234567");
    user.setRole(Role.ADMIN);
    userRepository.save(user);

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
  }

  @AfterEach
  public void afterEach() {
    productRepository.deleteAllInBatch();
    categoryRepository.deleteAllInBatch();
  }

  @AfterAll
  public void afterAll() {
    userRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    categoryRepository.deleteAllInBatch();
  }

  @Test
  void shouldGetAll() {
    given().when().get("/api/products").then().statusCode(HttpStatus.OK.value());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product (id, name, description, slug)
                  values (1, 'product', 'description', 'product');
                  """)
  void shouldGetOne() {
    given()
        .when()
        .get("/api/products/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "id",
            equalTo(1),
            "name",
            equalTo("product"),
            "description",
            equalTo("description"),
            "mainImage",
            equalTo(null),
            "category",
            equalTo(null));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product (id, name, description, slug)
                  values (1, 'product', 'description', 'product-slug');
                  """)
  void shouldGetOneBySlug() {
    given()
        .when()
        .get("/api/products/slug/product-slug")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "id",
            equalTo(1),
            "name",
            equalTo("product"),
            "description",
            equalTo("description"),
            "slug",
            equalTo("product-slug"),
            "mainImage",
            equalTo(null),
            "category",
            equalTo(null));
  }

  @Test
  @Sql(
      statements =
          """
                   insert into main.product_category(id, name, slug, parent_category_id)
                   values (1, 'category', 'category_path', null),
                  (2, 'childCategory', 'child_category_path', 1);
                   insert into main.product (id, name, description, slug, category_id)
                   values (1, 'product', 'description', 'product-slug', 2);
                   """)
  void shouldGetDetailsBySlug() {
    given()
        .when()
        .get("/api/products/slug/product-slug/details")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "product.id",
            equalTo(1),
            "product.name",
            equalTo("product"),
            "product.description",
            equalTo("description"),
            "product.slug",
            equalTo("product-slug"),
            "product.mainImage",
            equalTo(null),
            "product.category",
            notNullValue(),
            "variations",
            empty(),
            "breadcrumbs",
            notNullValue());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category(id, name, slug, parent_category_id)
                  values (1, 'category', 'category_path', null),
                         (2, 'childCategory', 'child_category_path', 1);
                  insert into main.product (id, name, description, category_id, slug)
                  values (1, 'product1', 'description1', 1, 'product1'),
                         (2, 'product2', 'description2', 2, 'product2');
                  """)
  void shouldGetByCategoryPath() {
    given()
        .when()
        .get("/api/products?categoryPath=category-path/child-category-path")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "_embedded.products.size()", equalTo(1),
            "_embedded.products[0].name", equalTo("product2"));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category(id, name, slug)
                  values (1, 'category', 'category');
                  insert into main.product (id, name, description, category_id, slug)
                  values (1, 'product', 'description', 1, 'product');
                  """)
  void shouldGetCategory() {
    given()
        .when()
        .get("/api/products/1/category")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(1), "name", equalTo("category"), "parentCategoryId", equalTo(null));
  }

  @Test
  void shouldNotSaveWhenNotAuthenticated() {
    ProductRequestDto productRequestDto =
        new ProductRequestDto("product", "description", null, null, null);

    given()
        .body(productRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/products")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotSaveWhenInvalidCategory() {
    ProductRequestDto productRequestDto =
        new ProductRequestDto("product", "description", 1L, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(productRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/products")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo("Category not found with id: 1"));
  }

  @Test
  void shouldSave() {
    ProductRequestDto productRequestDto =
        new ProductRequestDto("product", "description", null, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(productRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/products")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body(
            "id",
            notNullValue(),
            "name",
            equalTo("product"),
            "description",
            equalTo("description"));
  }

  @Test
  void shouldNotReplaceWhenNotAuthenticated() {
    ProductRequestDto productRequestDto =
        new ProductRequestDto("changed", "changed", null, null, null);

    given()
        .body(productRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/products/1")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotReplaceWhenProductDoesNotExist() {
    ProductRequestDto productRequestDto =
        new ProductRequestDto("changed", "changed", null, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(productRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/products/1")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo("Product not found with id: 1"));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product (id, name, description, slug)
                  values (1, 'product', 'description', 'product');
                  """)
  void shouldReplace() {
    ProductRequestDto productRequestDto =
        new ProductRequestDto("changed", "changed", null, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(productRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/products/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(1), "name", equalTo("changed"), "description", equalTo("changed"));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product (id, name, description, slug)
                  values (1, 'product', 'description', 'product');
                  """)
  void delete() {
    given()
        .auth()
        .oauth2(token)
        .when()
        .delete("/api/products/1")
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }
}
