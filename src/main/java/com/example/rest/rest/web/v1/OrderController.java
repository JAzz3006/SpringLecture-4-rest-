package com.example.rest.rest.web.v1;

import com.example.rest.rest.mapper.v1.OrderMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll(){
        return ResponseEntity.ok(
                orderMapper.orderListToOrderLIstResponse(orderService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById1(@PathVariable Long id){
        return ResponseEntity.ok(
                orderMapper.orderToResponse(orderService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody UpsertOrderRequest request){
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
