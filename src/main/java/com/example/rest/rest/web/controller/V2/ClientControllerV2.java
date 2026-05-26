package com.example.rest.rest.web.controller.V2;

import com.example.rest.rest.mapper.v2.ClientMapperV2;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.utils.BeanUtils;
import com.example.rest.rest.web.model.ClientListResponse;
import com.example.rest.rest.web.model.ClientResponse;
import com.example.rest.rest.web.model.UpsertClientRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/client")
@RequiredArgsConstructor
public class ClientControllerV2 {

    private final ClientService databaseClientService;
    private final ClientMapperV2 clientMapperV2;

    @GetMapping
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(
                clientMapperV2.clientListToClientListResponse(
                        databaseClientService.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id){
        return ResponseEntity
                .ok(clientMapperV2
                        .clientToResponse(
                                databaseClientService.findById(id)
                        )
                );
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid UpsertClientRequest request){
        Client client = databaseClientService.save(clientMapperV2.requestToClient(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        clientMapperV2.clientToResponse(client)
                );
    }

    @PostMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable Long id,
                                                 @RequestBody @Valid UpsertClientRequest request){
        Client updatedClient = databaseClientService.update(clientMapperV2.requestToClient(request));
        return ResponseEntity.ok(clientMapperV2.clientToResponse(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        databaseClientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}