package com.example.rest.rest.web.controller.v1;
import com.example.rest.rest.AbstractTestController;
import com.example.rest.rest.StringTestUtils;
import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.mapper.v1.OrderMapper;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends AbstractTestController {

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Test
    public void whenFindAll_thenReturnOrderListResponse() throws Exception {
       List<Order> orders = new ArrayList<>();
       Order order1 = createOrder(1L, 100L, null);
       Order order2 = createOrder(2L, 200L, null);
       orders.add(order1);
       orders.add(order2);

       List<OrderResponse> orderResponses = new ArrayList<>();
       OrderResponse orderResponse1 = createOrderResponse(1L, 100L);
       OrderResponse orderResponse2 = createOrderResponse(2L, 200L);
       orderResponses.add(orderResponse1);
       orderResponses.add(orderResponse2);

       OrderListResponse orderListResponse = new OrderListResponse(orderResponses);

       Mockito.when(orderService.findAll()).thenReturn(orders);
       Mockito.when(orderMapper.orderListToOrderLIstResponse(orders)).thenReturn(orderListResponse);

       String actualResponse = mockMvc.perform(get("/api/v1/order"))
               .andExpect(status().isOk())
               .andReturn()
               .getResponse()
               .getContentAsString();
       String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_orders_response.json");
       Mockito.verify(orderService, Mockito.times(1)).findAll();
       Mockito.verify(orderMapper, Mockito.times(1)).orderListToOrderLIstResponse(orders);
       JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFindOrderById_thenReturnOrderResponse() throws Exception{
        Order order = createOrder(1L, 100L, null);
        OrderResponse orderResponse = createOrderResponse(1L, 100L);
        Mockito.when(orderService.findById(1L)).thenReturn(order);
        Mockito.when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/order/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse = StringTestUtils.readStringFromResource("response/find_order_by_id_response.json");

        Mockito.verify(orderService, Mockito.times(1)).findById(1L);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(order);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateOrder_thenReturn() throws Exception{
        UpsertOrderRequest request = new UpsertOrderRequest(null, "Test product 1", new BigDecimal(100));
        Order order = new Order();
        order.setProduct("Test product 1");
        order.setCost(new BigDecimal(100));
        Order createdOrder = createOrder(1L, 100L, null);
        OrderResponse orderResponse = createOrderResponse(1L,100L);
        Mockito.when(orderService.save(order)).thenReturn(createdOrder);
        Mockito.when(orderMapper.requestToOrder(request)).thenReturn(order);
        Mockito.when(orderMapper.orderToResponse(createdOrder)).thenReturn(orderResponse);

        String actualResponse = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse = StringTestUtils.readStringFromResource("response/find_order_by_id_response.json");

        Mockito.verify(orderService, Mockito.times(1)).save(order);
        Mockito.verify(orderMapper, Mockito.times(1)).requestToOrder(request);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(createdOrder);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateOrder_thenReturnUpdatesOrder() throws Exception{
        UpsertOrderRequest request = new UpsertOrderRequest(null, "New product", new BigDecimal(200));
        Order order = createOrder(1L,200L, null);
        order.setProduct(request.getProduct());
        OrderResponse orderResponse = createOrderResponse(1L,200L);
        orderResponse.setProduct(request.getProduct());

        Mockito.when(orderService.update(order)).thenReturn(order);
        Mockito.when(orderMapper.requestToOrder(1L, request)).thenReturn(order);
        Mockito.when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);

        String actualResponse = mockMvc.perform(put("/api/v1/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse = StringTestUtils.readStringFromResource("response/update_order_response.json");

        Mockito.verify(orderService, Mockito.times(1)).update(order);
        Mockito.verify(orderMapper, Mockito.times(1)).requestToOrder(1L, request);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(order);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/order/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(orderService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenOrderByIdNotExistingOrder_thenReturnError () throws Exception{
        Mockito.when(orderService.findById(100L)).thenThrow(new EntityNotFoundException("Order with ID=100 not found"));

        String actualResponse = mockMvc.perform(get("/api/v1/order/100"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse = StringTestUtils.readStringFromResource("response/order_by_id_not_found_response.json");
        Mockito.verify(orderService, Mockito.times(1)).findById(100L);
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateOrderEmptyProduct_thenReturnError() throws Exception{
        UpsertOrderRequest request = new UpsertOrderRequest();
        String actualResponse = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpsertOrderRequest())))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse = StringTestUtils.readStringFromResource("response/empty_order_product_response.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}