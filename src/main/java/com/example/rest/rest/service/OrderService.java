package com.example.rest.rest.service;
import com.example.rest.rest.exception.UpdateStateException;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.web.model.OrderFilter;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(Long id);
    Order save(Order order);
    Order update(Order order);
    void deleteById(Long id);
    void deleteByIdIn(List<Long> ids);
    List<Order> filterBy(OrderFilter filter);

    default void checkForUpdate(Long id){
        Order currentOrder = findById(id);
        Instant now = Instant.now();
        Duration duration = Duration.between(currentOrder.getUpdatedAt(), now);
        if (duration.getSeconds() > 5){
            throw new UpdateStateException("Cannot update the order");
        }
    }
}