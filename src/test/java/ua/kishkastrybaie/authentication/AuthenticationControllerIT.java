package ua.kishkastrybaie.authentication;

import static org.hamcrest.Matchers.notNullValue;

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
import org.springframework.test.context.jdbc.Sql;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerIT {
  @LocalServerPort private int springBootPort;

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

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
  }

  @Test
  @Sql(
      statements =
          """
              delete from main."user"
              where email = 'user@user';
              """,
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldRegister() {
    RegisterRequest registerRequest =
        new RegisterRequest("user@user", "user", "user", "user@user", "380111111111", "longPassword");

    RestAssured.given()
        .body(registerRequest)
        .contentType(ContentType.JSON)
            .log().all()
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("token", notNullValue());
  }

  @Test
  void shouldAuthenticate() {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin@admin", "admin");
    RestAssured.given()
        .body(authenticationRequest)
        .contentType(ContentType.JSON)
        .when()
        .post("/api/auth/login")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("token", notNullValue());
  }

  @AfterAll
  public void tearDown() {
    userRepository.deleteAll();
  }
}
