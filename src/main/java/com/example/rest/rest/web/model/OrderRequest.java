package com.example.rest.rest.web.model;
import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderRequest {
    private String product;
    private BigDecimal cost;
}