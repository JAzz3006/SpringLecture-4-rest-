package com.example.rest.rest.mapper.v2;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class OrderMapperDelegate implements OrderMapperV2{

    @Autowired
    private ClientService databaseClientService;

    @Override
    public Order requestToOrder(UpsertOrderRequest request){
        Order order = new Order();
        order.setProduct(request.getProduct());
        order.setCost(request.getCost());
        Client client = databaseClientService.findById(request.getClientId());
        order.setClient(client);
        return order;
    }

    @Override
    public Order requestToOrder(Long orderId, UpsertOrderRequest request){
        Order order = requestToOrder(request);
        order.setId(orderId);
        return order;
    }
}