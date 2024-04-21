package ua.kishkastrybaie.user;

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
import ua.kishkastrybaie.authentication.AuthenticationRequest;
import ua.kishkastrybaie.authentication.AuthenticationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AuthenticationService authenticationService;
  private String token = "";

  @BeforeAll
  public void setup() {
    RestAssured.port = springBootPort;

    User user = new User();
    user.setEmail("user@user");
    user.setPassword(passwordEncoder.encode("user"));
    user.setFirstName("user");
    user.setPhoneNumber("+380501234567");
    user.setRole(Role.USER);
    userRepository.save(user);

    AuthenticationRequest request = new AuthenticationRequest("user@user", "user");
    token = authenticationService.authenticate(request).accessToken();
  }

  @AfterAll
  public void afterAll() {
    userRepository.deleteAll();
  }

  @Test
  @Order(1)
  void shouldGetCurrentUser() {
    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .when()
        .get("/api/users/me")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(
            "email",
            equalTo("user@user"),
            "firstName",
            equalTo("user"),
            "phoneNumber",
            equalTo("+380501234567"));
  }

  @Test
  @Order(2)
  void shouldUpdateUser() {
    UserRequestDto userRequestDto =
        new UserRequestDto("admin2", "admin2", "admin2", "user@user", "+380501234567");

    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .body(userRequestDto)
        .when()
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  @Order(3)
  void shouldNotUpdateUserWhenInvalidEmail() {
    UserRequestDto userRequestDto =
        new UserRequestDto("admin2", "admin2", "admin2", "invalidEmail", "+380501234567");

    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .body(userRequestDto)
        .when()
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @Order(4)
  void shouldNotUpdateUserWhenInvalidPhoneNumber() {
    UserRequestDto userRequestDto =
        new UserRequestDto("admin2", "admin2", "admin2", "admin@admin", "invalidNumber");

    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .body(userRequestDto)
        .when()
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @Order(5)
  void shouldNotChangePasswordWhenInvalidCurrentPassword() {
    ChangePasswordRequest changePasswordRequest =
        new ChangePasswordRequest("invalidPassword", "admin2");

    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .body(changePasswordRequest)
        .when()
        .put("/api/users/me/password")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @Order(6)
  void shouldNotChangePasswordWhenInvalidNewPassword() {
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("user", "short");

    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .body(changePasswordRequest)
        .when()
        .put("/api/users/me/password")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @Order(7)
  void shouldChangePassword() {
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("user", "megaPassword");

    given()
        .auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .body(changePasswordRequest)
        .when()
        .put("/api/users/me/password")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  void shouldNotGetCurrentUserWhenUnauthorized() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/users/me")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotUpdateUserWhenUnauthorized() {
    UserRequestDto userRequestDto =
        new UserRequestDto("admin2", "admin2", "admin2", "admin@admin", "+380501234567");

    given()
        .contentType(ContentType.JSON)
        .body(userRequestDto)
        .when()
        .put("/api/users/me")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldNotChangePasswordWhenUnauthorized() {
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("user", "megaPassword");

    given()
        .contentType(ContentType.JSON)
        .body(changePasswordRequest)
        .when()
        .put("/api/users/me/password")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }
}
