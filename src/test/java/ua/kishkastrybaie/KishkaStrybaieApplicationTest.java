package ua.kishkastrybaie;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class KishkaStrybaieApplicationTest {

  @Test
  void contextLoads(ApplicationContext context) {
    assertThat(context).isNotNull();
  }
}
