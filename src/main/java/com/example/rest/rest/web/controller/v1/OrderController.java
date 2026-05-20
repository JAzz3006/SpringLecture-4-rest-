package com.example.rest.rest.web.controller.v1;
import com.example.rest.rest.mapper.v1.OrderMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.model.ErrorResponse;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Tag(name = "Order v1", description = "Order API version v1")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(
            summary = "Finds all orders",
            description = "Finds all orders",
            tags = {"order"}
    )
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(schema = @Schema(implementation = OrderListResponse.class), mediaType = "application/json")
            }
    )
    @GetMapping
    public ResponseEntity<OrderListResponse> findAll(){
        return ResponseEntity.ok(
                orderMapper.orderListToOrderLIstResponse(orderService.findAll())
        );
    }

    @Operation(
            summary = "Finds order by id",
            description = "Finds order by id",
            tags = {"order", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById1(@PathVariable Long id){
        return ResponseEntity.ok(
                orderMapper.orderToResponse(orderService.findById(id))
        );
    }

    @Operation(
            summary = "Creates order",
            description = "Creates order. Product name is necessary",
            tags = {"order"}
    )
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody UpsertOrderRequest request){
        Order newOrder = orderService.save(orderMapper.requestToOrder(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.orderToResponse(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(
            @PathVariable("id") Long orderId,
            @RequestBody UpsertOrderRequest request){
        Order updatedOrder = orderService.update(orderMapper.requestToOrder(orderId, request));
        return ResponseEntity.ok(orderMapper.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        orderService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
