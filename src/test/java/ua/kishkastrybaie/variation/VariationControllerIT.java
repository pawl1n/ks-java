package ua.kishkastrybaie.variation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
import ua.kishkastrybaie.authentication.AuthenticationService;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VariationControllerIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private VariationRepository variationRepository;
  @Autowired private AuthenticationService authenticationService;
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

    AuthenticationRequest request = new AuthenticationRequest("admin@admin", "admin");
    token = authenticationService.authenticate(request).accessToken();
  }

  @AfterEach
  public void afterEach() {
    variationRepository.deleteAll();
  }

  @AfterAll
  public void afterAll() {
    userRepository.deleteAll();
    variationRepository.deleteAll();
  }

  @Test
  void shouldGetAll() {
    given().when().get("/api/variations").then().statusCode(HttpStatus.OK.value());
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.variation (id, name)
                  values (1, 'variation');
                  """)
  void shouldGetOne() {
    given()
        .when()
        .get("/api/variations/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "id",
            equalTo(1),
            "name",
            equalTo("variation"));
  }

  @Test
  void shouldNotSaveWhenNotAuthenticated() {
    VariationRequestDto variationRequestDto =
        new VariationRequestDto("variation");

    given()
        .body(variationRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/variations")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldSave() {
    VariationRequestDto variationRequestDto =
        new VariationRequestDto("variation");

    given()
        .auth()
        .oauth2(token)
        .body(variationRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/variations")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("id", equalTo(1), "name", equalTo("variation"));
  }

  @Test
  void shouldNotReplaceWhenNotAuthenticated() {
    VariationRequestDto variationRequestDto = new VariationRequestDto("changed");

    given()
        .body(variationRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/variations/1")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotReplaceWhenVariationDoesNotExist() {
    VariationRequestDto variationRequestDto = new VariationRequestDto("changed");

    given()
        .auth()
        .oauth2(token)
        .body(variationRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/variations/1")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("message", equalTo("Variation with id 1 not found"));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.variation (id, name)
                  values (1, 'variation');
                  """)
  void shouldReplace() {
    VariationRequestDto variationRequestDto = new VariationRequestDto("changed");

    given()
        .auth()
        .oauth2(token)
        .body(variationRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/variations/1")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(1), "name", equalTo("changed"));
  }

  @Test
  @Sql(
      statements =
          """
                  insert into main.variation (id, name)
                  values (1, 'variation');
                  """)
  void delete() {
    given()
        .auth()
        .oauth2(token)
        .when()
        .delete("/api/variations/1")
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }
}
