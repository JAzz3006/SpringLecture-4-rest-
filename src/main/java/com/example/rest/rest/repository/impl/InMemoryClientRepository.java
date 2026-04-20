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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryClientRepository implements ClientRepository {

    private OrderRepository orderRepository;
    private final Map<Long, Client> repository = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Client> findByID(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Client save(Client client) {
        Long clientId = currentId.getAndIncrement();
        client.setId(clientId);
        repository.put(clientId, client);
        return client;
    }

    @Override
    public Client update(Client client) {
        Long currentId = client.getId();
        Client currentClient = repository.get(currentId);
        if (currentClient == null){
            throw new EntityNotFoundException(MessageFormat.format("Client with ID {0} not found", currentId));
        }
        BeanUtils.copyNonNullProperties(client, currentClient);
        repository.put(client.getId(), currentClient);
        return currentClient;
    }

    @Override
    public void deleteById(Long id) {
        Client client = repository.get(id);
        if (client == null){
            throw new EntityNotFoundException(MessageFormat.format("Client with ID {0} not found", currentId));
        }
        orderRepository.deleteByIdIn(client.getOrders().stream()
                .map(Order::getId)
                .toList());
        repository.remove(id);
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
}