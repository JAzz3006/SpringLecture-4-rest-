package com.example.rest.rest.repository;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.web.model.OrderFilter;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.Instant;

public interface OrderSpecification {
    static Specification<Order> withFilter(OrderFilter orderFilter){
        return Specification.where(byProductName(orderFilter.getProductName()))
                .and(byCostRange(orderFilter.getMinCost(), orderFilter.getMaxCost()))
                .and(byClientId(orderFilter.getClientId()))
                .and(byCreatedAtBefore(orderFilter.getCreatedBefore()))
                .and(byUpdatedAtBefore(orderFilter.getUpdatedBefore()));
    }

    static Specification<Order> byProductName (String productName){
        return (root, query, cb) -> {
            if (productName == null) return null;
            return cb.equal(root.get("product"), productName);
        };
    }

    static Specification<Order> byCostRange (BigDecimal minCost, BigDecimal maxCost){
        return (root, query, cb) -> {
            if (minCost == null && maxCost == null) return null;
            if (minCost == null) return cb.lessThanOrEqualTo(root.get("cost"), maxCost);
            if (maxCost == null) return cb.greaterThanOrEqualTo(root.get("cost"), minCost);
            if (minCost.longValue() > maxCost.longValue()) return null;
            return cb.between(root.get("cost"), minCost, maxCost);
        };
    }

    static Specification<Order> byClientId (Long clientId){
        return (root, query, cb) -> {
            if (clientId == null) return null;
            return cb.equal(root.get("client").get("id"), clientId);
        };
    }

    static Specification<Order> byCreatedAtBefore (Instant createdBefore){
        return (root, query, cb) -> {
            if (createdBefore == null) return null;
            return cb.lessThan(root.get("createdAt"), createdBefore);
        };
    }

    static Specification<Order> byUpdatedAtBefore (Instant updatedBefore){
        return (root, query, cb) -> {
            if (updatedBefore == null) return null;
            return cb.lessThanOrEqualTo(root.get("updatedAt"), updatedBefore);
        };
    }
}