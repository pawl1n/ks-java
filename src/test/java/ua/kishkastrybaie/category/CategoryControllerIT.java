package ua.kishkastrybaie.category;

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
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private CategoryRepository categoryRepository;
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
    categoryRepository.deleteAllInBatch();
  }

  @AfterAll
  public void afterAll() {
    userRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  void shouldGetAll() {
    given().when().get("/api/categories").then().statusCode(HttpStatus.OK.value());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name)
                  values (1, 'category');
                  """)
  void shouldGetOne() {
    given()
        .when()
        .get("/api/categories/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(1), "name", equalTo("category"), "parentCategory", equalTo(null));
  }

  @Test
  void shouldNotSaveWhenNotAuthenticated() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null);

    given()
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/products")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotSaveWhenInvalidParentCategory() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", 1L);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/categories")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo("Category not found with id: 1"));
  }

  @Test
  void shouldSave() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/categories")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("id", notNullValue(), "name", equalTo("category"));
  }

  @Test
  void shouldNotReplaceWhenNotAuthenticated() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null);

    given()
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/categories/1")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotReplaceWhenProductDoesNotExist() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/categories/1")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo("Category not found with id: 1"));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name)
                  values (1, 'category');
                  """)
  void shouldReplace() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("changed", null);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/categories/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(1), "name", equalTo("changed"));
  }

  @Test
  @Sql(
      statements =
          """
        insert into main.product_category (id, name)
        values (1, 'category');
        """)
  void shouldDelete() {
    given()
        .auth()
        .oauth2(token)
        .when()
        .delete("/api/categories/1")
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  @Sql(
      statements =
          """
      insert into main.product_category (id, name, parent_category_id)
      values (1, 'category', null),
      (2, 'children', 1),
      (3, 'grandchildren', 2);
      """)
  void shouldGetAllDescendents() {
    given().when().get("/api/categories/1/children").then().statusCode(HttpStatus.OK.value());
  }
}
