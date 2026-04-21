package com.example.rest.rest.model;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

@Data
public class Client {
    private Long id;
    private String name;
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order){
        orders.add(order);
    }

    public void removeOrder(Long id){
         orders = orders.stream()
                 .filter(o -> !o.getId().equals(id))
                 .toList();
    }
}