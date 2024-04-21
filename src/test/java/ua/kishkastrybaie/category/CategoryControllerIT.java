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

    userRepository.deleteAllInBatch();

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
                  insert into main.product_category (id, name, parent_category_id, slug)
                  values (1, 'category', null, 'category'),
                  (2, 'children', 1, 'children'),
                  (3, 'grandchildren', 2, 'grandchildren');
                  """)
  void shouldGetRootCategories() {
    given()
        .when()
        .get("/api/categories/root")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("_embedded.categories.size()", equalTo(1));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name, parent_category_id, slug)
                  values (1, 'category', null, 'category'),
                  (2, 'children', 1, 'children'),
                  (3, 'grandchildren', 2, 'grandchildren');
                  """)
  void shouldGetTree() {
    given()
        .when()
        .get("/api/categories/tree")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "_embedded.categories.size()", equalTo(1),
            "_embedded.categories[0].descendants.size()", equalTo(1),
            "_embedded.categories[0].descendants[0].descendants.size()", equalTo(1),
            "_embedded.categories[0].descendants[0].descendants[0].descendants", empty());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name, slug)
                  values (1, 'category', 'category');
                  """)
  void shouldGetOneById() {
    given()
        .when()
        .get("/api/categories/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(1), "name", equalTo("category"), "parentCategory", equalTo(null));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name, parent_category_id, slug)
                  values (1, 'category', null, 'category'),
                  (2, 'children', 1, 'children'),
                  (3, 'grandchildren', 2, 'grandchildren');
                          """)
  void shouldGetOneByPath() {
    given()
        .when()
        .get("/api/categories/path/category/children/grandchildren")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(3), "name", equalTo("grandchildren"));
  }

  @Test
  void shouldNotSaveWhenNotAuthenticated() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null, null, null);

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
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", 1L, null, null);

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
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/categories")
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  void shouldNotReplaceWhenNotAuthenticated() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null, null, null);

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
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", null, null, null);

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
                  insert into main.product_category (id, name, slug)
                  values (1, 'category', 'category');
                  """)
  void shouldReplace() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("changed", null, null, null);

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
                  insert into main.product_category (id, name, slug)
                  values (1, 'category', 'category');
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
                  insert into main.product_category (id, name, parent_category_id, slug)
                  values (1, 'category', null, 'category'),
                  (2, 'children', 1, 'children'),
                  (3, 'grandchildren', 2, 'grandchildren');
                  """)
  void shouldGetAllDescendents() {
    given().when().get("/api/categories/1/descendants").then().statusCode(HttpStatus.OK.value());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name, parent_category_id, slug)
                  values (1, 'category', null, 'category'),
                  (2, 'children', 1, 'children'),
                  (3, 'grandchildren', 2, 'grandchildren');
                  """)
  void shouldNotMoveCategoryWhenMoveToDescendant() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", 3L, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/categories/1")
        .then()
        .statusCode(HttpStatus.CONFLICT.value());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.product_category (id, name, parent_category_id, slug)
                  values (1, 'category', null, 'category'),
                  (2, 'children', 1, 'children'),
                  (3, 'grandchildren', 2, 'grandchildren');
                  """)
  void shouldNotMoveCategoryWhenMoveToItself() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("category", 1L, null, null);

    given()
        .auth()
        .oauth2(token)
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/categories/1")
        .then()
        .statusCode(HttpStatus.CONFLICT.value());
  }
}
