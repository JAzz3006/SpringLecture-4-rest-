package com.example.rest.rest.repository.impl;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.ClientRepository;
import com.example.rest.rest.repository.OrderRepository;
import com.example.rest.rest.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.rest.rest.exception.EntityNotFoundException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    private ClientRepository clientRepository;
    private Map<Long, Order> repository = new ConcurrentHashMap<>();

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Order save(Order order) {
        Long orderId = currentId.getAndIncrement();
        Long clientId = order.getClient().getId();
        Client client = clientRepository.findByID(clientId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Client with ID = {0} not found", clientId)));
        order.setClient(client);
        order.setId(orderId);
        Instant now = Instant.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        repository.put(orderId, order);
        client.addOrder(order);
        clientRepository.update(client);
        return order;
    }

    @Override
    public Order update(Order order) {
        Long orderId = order.getId();
        Order currentOrder = repository.get(orderId);
        if (currentOrder == null){
            throw new EntityNotFoundException(MessageFormat.format("Order with ID = {0} not found", orderId));
        }
        BeanUtils.copyNonNullProperties(order, currentOrder);
        currentOrder.setUpdatedAt(Instant.now());
        currentOrder.setId(orderId);
        repository.put(orderId, order);
        return order;
    }

    @Override
    public void deleteById(Long id) {
        repository.remove(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        ids.forEach(repository::remove);
    }
}