package com.example.rest.rest.web.controller.V2;
import com.example.rest.rest.mapper.v2.OrderMapperV2;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.service.impl.DataBaseOrderService;
import com.example.rest.rest.web.model.OrderFilter;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/order")
public class OrderControllerV2 {

    private final OrderMapperV2 orderMapperV2;
    private final DataBaseOrderService databaseOrderService;

    @GetMapping("/filter")
    public ResponseEntity<OrderListResponse> filterBy(OrderFilter filter){
        return ResponseEntity.ok(
                orderMapperV2.orderListToOrderListResponse(
                        databaseOrderService.filterBy(filter)
                ));
    }

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll(){
        return ResponseEntity.ok(orderMapperV2.orderListToOrderListResponse(databaseOrderService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderMapperV2.orderToResponse(databaseOrderService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request){
        Order newOrder = databaseOrderService.save(orderMapperV2.requestToOrder(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapperV2.orderToResponse(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update (
            @PathVariable Long id,
            @RequestBody @Valid UpsertOrderRequest request){
        Order updatedOrder = databaseOrderService.update(orderMapperV2.requestToOrder(id, request));
        return ResponseEntity.ok(orderMapperV2.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        databaseOrderService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
