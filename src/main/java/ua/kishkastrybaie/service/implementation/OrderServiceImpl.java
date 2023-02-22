package ua.kishkastrybaie.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.exception.OrderNotFoundException;
import ua.kishkastrybaie.repository.OrderRepository;
import ua.kishkastrybaie.repository.entity.Order;
import ua.kishkastrybaie.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order order) {
        return null;
    }

    @Override
    public Order replace(Long id, Order order) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
