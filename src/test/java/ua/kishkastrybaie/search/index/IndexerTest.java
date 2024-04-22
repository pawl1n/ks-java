package ua.kishkastrybaie.search.index;

import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kishkastrybaie.product.Product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexerTest {
    @Mock
    EntityManager entityManager;
    @InjectMocks Indexer indexer;

  @Test
  void shouldIndexPersistedData() throws IndexException, InterruptedException {
        // given
        SearchSession searchSession = mock(SearchSession.class);
        MassIndexer massIndexer = mock(MassIndexer.class);

        try (MockedStatic<Search> search = mockStatic(Search.class)) {
            // when
            search.when(() -> Search.session(entityManager)).thenReturn(searchSession);
            when(searchSession.massIndexer(Product.class)).thenReturn(massIndexer);

            indexer.indexPersistedData(Product.class);

            // then
            verify(massIndexer).startAndWait();
        }
    }

  @Test
  void shouldNotIndexPersistedData() throws IndexException, InterruptedException {
      // given
      SearchSession searchSession = mock(SearchSession.class);
      MassIndexer massIndexer = mock(MassIndexer.class);

      try (MockedStatic<Search> search = mockStatic(Search.class)) {
          // when
          search.when(() -> Search.session(entityManager)).thenReturn(searchSession);
          when(searchSession.massIndexer(Product.class)).thenReturn(massIndexer);
          doThrow(InterruptedException.class).when(massIndexer).startAndWait();

          // then
          assertThatThrownBy(() -> indexer.indexPersistedData(Product.class))
              .isInstanceOf(IndexException.class);
      }
  }
}
