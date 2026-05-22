package com.example.rest.rest.repository;
import com.example.rest.rest.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataBaseOrderRepository extends JpaRepository<Order, Long> {
}