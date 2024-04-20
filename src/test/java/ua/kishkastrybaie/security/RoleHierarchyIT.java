package ua.kishkastrybaie.security;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kishkastrybaie.authentication.AuthenticationRequest;
import ua.kishkastrybaie.authentication.AuthenticationService;
import ua.kishkastrybaie.category.CategoryRepository;
import ua.kishkastrybaie.category.CategoryRequestDto;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;
import ua.kishkastrybaie.user.UserRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleHierarchyIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AuthenticationService authenticationService;
  @Autowired private CategoryRepository categoryRepository;
  private String adminAccessToken = "";
  private String userAccessToken = "";

  @BeforeAll
  public void setup() {
    RestAssured.port = springBootPort;

    userRepository.deleteAllInBatch();

    User admin = new User();
    admin.setEmail("admin@admin");
    admin.setPassword(passwordEncoder.encode("administrator"));
    admin.setFirstName("administrator");
    admin.setLastName("administrator");
    admin.setPhoneNumber("+380501234567");
    admin.setRole(Role.ADMIN);
    userRepository.save(admin);

    AuthenticationRequest adminAuthenticationRequest =
        new AuthenticationRequest("admin@admin", "administrator");
    adminAccessToken = authenticationService.authenticate(adminAuthenticationRequest).accessToken();

    User user = new User();
    user.setEmail("user@user");
    user.setPassword(passwordEncoder.encode("userPassword"));
    user.setFirstName("user");
    user.setLastName("user");
    user.setPhoneNumber("+380661234567");
    user.setRole(Role.USER);
    userRepository.save(user);

    AuthenticationRequest userAuthenticationRequest =
        new AuthenticationRequest("user@user", "userPassword");
    userAccessToken = authenticationService.authenticate(userAuthenticationRequest).accessToken();
  }

  @AfterAll
  public void tearDown() {
    userRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  void shouldAccessAdminEndpointWhenAdmin() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test", null, null, null);

    RestAssured.given()
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .auth()
        .oauth2(adminAccessToken)
        .post("/api/categories")
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  void shouldNotAccessAdminEndpointWhenUser() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test", null, null, null);

    RestAssured.given()
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .auth()
        .oauth2(userAccessToken)
        .post("/api/categories")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  void shouldNotAccessAdminEndpointWhenUnauthenticated() {
    CategoryRequestDto categoryRequestDto = new CategoryRequestDto("test", null, null, null);

    RestAssured.given()
        .body(categoryRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/categories")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldAccessUserEndpointWhenUser() {
    UserRequestDto userRequestDto =
        new UserRequestDto("user", "user", "user", "user@user", "+380661234567");

    RestAssured.given()
        .body(userRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .auth()
        .oauth2(userAccessToken)
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  void shouldAccessUserEndpointWhenAdmin() {
    UserRequestDto userRequestDto =
        new UserRequestDto("admin", "admin", "admin", "admin@admin", "+380501234567");

    RestAssured.given()
        .body(userRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .auth()
        .oauth2(adminAccessToken)
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  void shouldNotAccessUserEndpointWhenUnauthenticated() {
    UserRequestDto userRequestDto =
        new UserRequestDto("user", "user", "user", "user@user", "+380951234567");

    RestAssured.given()
        .body(userRequestDto)
        .contentType(ContentType.JSON)
        .when()
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }
}
