package com.example.rest.rest.web.model;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderListResponse {
    private List<OrderResponse> orders = new ArrayList<>();
}