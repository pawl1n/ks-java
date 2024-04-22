package ua.kishkastrybaie.search.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.product.Product;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexRunner implements ApplicationRunner {
  private final Indexer indexer;

  @Override
  public void run(ApplicationArguments args) {
    indexer.indexPersistedData(Product.class);
  }
}
