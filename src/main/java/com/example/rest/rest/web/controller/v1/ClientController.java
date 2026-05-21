package com.example.rest.rest.web.controller.v1;
import com.example.rest.rest.mapper.v1.ClientMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.model.ClientListResponse;
import com.example.rest.rest.web.model.ClientResponse;
import com.example.rest.rest.web.model.ErrorResponse;
import com.example.rest.rest.web.model.UpsertClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/client")
@RequiredArgsConstructor
@Tag(name = "Client v1", description = "Client API version v1")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @Operation(
            summary = "Gets all clients",
            description = "Gets all clients",
            tags = {"client"}
    )
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(schema = @Schema(implementation = ClientListResponse.class), mediaType = "application/json")
            }
    )
    @GetMapping()
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(
                clientMapper.clientListToClientResponseList(clientService.findAll())
        );
    }

    @Operation(
            summary = "Gets client by id",
            description = "Gets client by id, returns id, name and list of orders",
            tags = {"client"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = ClientResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(
                clientMapper.clientToResponse(clientService.findById(id))
        );
    }

    @Operation(
            summary = "Saves a new client by name",
            description = "Saves a new client by name, name must be more than 3 and less than 30 symbols",
            tags = {"client"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = ClientResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),  mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid UpsertClientRequest request){
        Client newClient = clientService.save(clientMapper.requestToClient(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientMapper.clientToResponse(newClient));
    }

    @Operation(
            summary = "Updates client by id",
            description = "Updates client by id, name update only",
            tags = {"client"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = ClientResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable("id") Long clientId,
                                                 @RequestBody @Valid UpsertClientRequest request){
        Client updatedClient = clientService.update(clientMapper.requestToClient(clientId, request));
        ClientResponse response = clientMapper.clientToResponse(updatedClient);
        return ResponseEntity
                .ok(response);
    }

    @Operation(
            summary = "Deletes client by id",
            description = "Deletes client by id",
            tags = {"client"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        clientService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Void> notFoundHandler(EntityNotFoundException ex){
//        return ResponseEntity.notFound().build();
//    }
}