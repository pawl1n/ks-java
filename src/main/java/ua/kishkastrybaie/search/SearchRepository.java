package ua.kishkastrybaie.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface SearchRepository<T, I extends Serializable> extends JpaRepository<T, I> {
    Page<T> search(String text, Pageable pageable);
}
