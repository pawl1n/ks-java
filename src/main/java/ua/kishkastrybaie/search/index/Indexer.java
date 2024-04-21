package ua.kishkastrybaie.search.index;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class Indexer {
  private final EntityManager entityManager;

  public void indexPersistedData(Class<?> classToIndex) throws IndexException {
    try {
      SearchSession searchSession = Search.session(entityManager);

      MassIndexer indexer =
          searchSession.massIndexer(classToIndex);

      indexer.startAndWait();
    } catch (InterruptedException e) {
      throw new IndexException("Index Interrupted", e);
    }
  }
}
