package com.example.rest.rest.web.model;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateClientWithOrderRequest {
    private String name;
    private List<OrderRequest> orders;
}