package ua.kishkastrybaie.service;

import ua.kishkastrybaie.repository.entity.Order;

public interface OrderService {
    Iterable<Order> findAll();
    Order findById(Long id);
    Order create(Order order);
    Order update(Long id, Order order);
    Order replace(Long id, Order order);
    void deleteById(Long id);
}
