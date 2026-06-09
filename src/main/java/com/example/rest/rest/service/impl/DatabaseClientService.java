package com.example.rest.rest.service.impl;
import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.DataBaseClientRepository;
import com.example.rest.rest.repository.DataBaseOrderRepository;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseClientService implements ClientService {

    private final DataBaseClientRepository dataBaseClientRepository;
    private final DataBaseOrderRepository dataBaseOrderRepository;

    @Override
    public List<Client> findAll() {
        return dataBaseClientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return dataBaseClientRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Client with ID=%d not found", id)));
    }

    @Override
    public Client save(Client client) {
        return dataBaseClientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
       Client currentClient = dataBaseClientRepository.findById(client.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Client with ID = %d not found", client.getId())));
        BeanUtils.copyNonNullProperties(client, currentClient);
        return dataBaseClientRepository.save(currentClient);
    }

    @Override
    public void deleteById(Long id) {
        dataBaseClientRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Client with ID=%d not found", id)));
        dataBaseClientRepository.deleteById(id);
    }

    @Override
    public Client saveWithOrders(Client client, List<Order> orders) {
        Client savedClient = dataBaseClientRepository.save(client);
        for (Order order : orders){
            order.setClient(savedClient);
            Order savedOrder = dataBaseOrderRepository.save(order);
            savedClient.addOrder(savedOrder);
        }
        return savedClient;
    }
}