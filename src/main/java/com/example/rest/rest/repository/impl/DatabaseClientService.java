package com.example.rest.rest.repository.impl;
import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.repository.ClientRepository;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseClientService implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findByID(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Client with ID=%d not found", id)));
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        Long currentId = client.getId();
        Client currentClient = clientRepository.findByID(client.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Client with id=%d not found", currentId)));
        BeanUtils.copyNonNullProperties(currentClient, client);
        return clientRepository.save(currentClient);
    }



    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
