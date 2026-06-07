package com.example.rest.rest.repository;
import com.example.rest.rest.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DataBaseOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findAllByProduct(String product, Pageable pageable);

//    @Query("SELECT o FROM com.example.rest.rest.model.Order o WHERE o.product = :productName")
    @Query(value = "SELECT * FROM orders o WHERE o.product = :productName", nativeQuery = true)
    List<Order> getByProduct(String productName);
}