package com.example.rest.rest.mapper.v1;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.web.model.UpsertClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final OrderMapper orderMapper;

    public Client requestToClient(UpsertClientRequest upsertClientRequest){
        Client client = new Client();
        client.setName(upsertClientRequest.getName());
        return client;
    }

    public Client requestToClient(Long clientId, UpsertClientRequest upsertClientRequest){
        Client client = requestToClient(upsertClientRequest);
        client.setId(clientId);
        return client;
    }
}