package com.example.rest.rest.mapper.v1;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ClientService clientService;

    public Order requestToOrder(UpsertOrderRequest upsertOrderRequest){
        Order order = new Order();
        order.setCost(upsertOrderRequest.getCost());
        order.setProduct(upsertOrderRequest.getProduct());
        order.setClient(clientService.findById(upsertOrderRequest.getClientId()));
        return order;
    }

    public Order requestToOrder(Long orderId, UpsertOrderRequest upsertOrderRequest){
        Order order = requestToOrder(upsertOrderRequest);
        order.setId(orderId);
        return order;
    }

    public OrderResponse orderToResponse(Order order){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setCost(order.getCost());
        orderResponse.setProduct(order.getProduct());
        return orderResponse;
    }

    public List<OrderResponse> orderListToResponseList(List<Order> orders){
        return orders.stream()
                .map(this::orderToResponse)
                .toList();
    }

    public OrderListResponse orderListToOrderLIstResponse(List<Order> orders){
        OrderListResponse orderListResponse = new OrderListResponse();
        orderListResponse.setOrders(orderListToResponseList(orders));
        return orderListResponse;
    }
}