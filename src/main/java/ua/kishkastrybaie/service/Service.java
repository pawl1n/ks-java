package ua.kishkastrybaie.service;

import java.util.List;

public interface Service<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T create(T entity);
    T update(ID id, T entity);
    T replace(ID id, T entity);
    void deleteById(ID id);
}
