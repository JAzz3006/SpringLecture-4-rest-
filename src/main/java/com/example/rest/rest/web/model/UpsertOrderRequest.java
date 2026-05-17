package com.example.rest.rest.web.model;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertOrderRequest {
    private Long clientId;
    @NotBlank(message = "Product name must not be blank")
    private String product;
    private BigDecimal cost;
}