package com.example.rest.rest.web.model;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertClientRequest {
    @NotBlank(message = "Client name must be filled in")
    private String name;
}