package ua.kishkastrybaie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.kishkastrybaie.search.SearchRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class KishkaStrybaieApplication {

  public static void main(String[] args) {
    SpringApplication.run(KishkaStrybaieApplication.class, args);
  }
}
