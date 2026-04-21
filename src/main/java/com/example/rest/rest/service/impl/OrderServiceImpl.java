package com.example.rest.rest.service.impl;
import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.exception.UpdateStateException;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.ClientRepository;
import com.example.rest.rest.repository.OrderRepository;
import com.example.rest.rest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
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
                .orElseThrow(()->new EntityNotFoundException(
                        MessageFormat.format("Order with ID={0} not found", id)));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        return orderRepository.update(order);
    }

    @Override
    public void deleteById(Long id) {
        Order currentOrder = findById(id);
        currentOrder.getClient().removeOrder(id);
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteByIDIn(List<Long> ids) {
        orderRepository.deleteByIdIn(ids);
    }

    private void checkForUpdate(Long id){
        Order currentOrder = findById(id);
        Instant now = Instant.now();
        Duration duration = Duration.between(currentOrder.getUpdatedAt(), now);
        if (duration.getSeconds() > 5){
            throw new UpdateStateException("Cannot update the order");
        }
    }
}
