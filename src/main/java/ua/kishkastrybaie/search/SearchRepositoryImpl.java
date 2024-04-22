package ua.kishkastrybaie.search;

import jakarta.persistence.EntityManager;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

@Transactional
public class SearchRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
    implements SearchRepository<T, I> {
  private final EntityManager entityManager;

  public SearchRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);

    this.entityManager = entityManager;
  }

  @Override
  public Page<T> search(String text, Pageable pageable) {
    SearchResult<T> searchResult = getSearchResult(text, pageable);

    return new PageImpl<>(searchResult.hits(), pageable, searchResult.total().hitCount());
  }

  private SearchResult<T> getSearchResult(String text, Pageable pageable) {
    SearchSession searchSession = Search.session(entityManager);

    String[] fields =
        Arrays.stream(getDomainClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(FullTextField.class))
            .map(Field::getName)
            .toArray(String[]::new);

    return searchSession
        .search(getDomainClass())
        .where(f -> f.match().fields(fields).matching(text).fuzzy())
        .fetch(pageable.getPageNumber(), pageable.getPageSize());
  }
}
