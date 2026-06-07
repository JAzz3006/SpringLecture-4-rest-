package com.example.rest.rest.service.impl;
import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.DataBaseClientRepository;
import com.example.rest.rest.repository.DataBaseOrderRepository;
import com.example.rest.rest.repository.OrderSpecification;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.utils.BeanUtils;
import com.example.rest.rest.web.model.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseOrderService implements OrderService {

    private final DataBaseOrderRepository dataBaseOrderRepository;
    private final DataBaseClientRepository dataBaseClientRepository;


    @Override
    public List<Order> filterBy(OrderFilter filter){
//        return dataBaseOrderRepository.getByProduct(filter.getProductName());
        return dataBaseOrderRepository.findAll(OrderSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())).getContent();
    }

    @Override
    public List<Order> findAll() {
        return dataBaseOrderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return dataBaseOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with ID=%d not found", id)));
    }

    @Override
    public Order save(Order order) {
        Client currentClient = dataBaseClientRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Client with ID=%d not found", order.getClient().getId())));
        order.setClient(currentClient);
        return dataBaseOrderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        Client currentClient =dataBaseClientRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Client with ID=%d not found", order.getClient().getId())));
        Order currentOrder = dataBaseOrderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with ID=%d not found", order.getId())));
        BeanUtils.copyNonNullProperties(order, currentOrder);
        currentOrder.setClient(currentClient);
        return dataBaseOrderRepository.save(currentOrder);
    }

    @Override
    public void deleteById(Long id) {
        dataBaseOrderRepository.findById(id);
        dataBaseOrderRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        dataBaseOrderRepository.deleteAllById(ids);
    }
}