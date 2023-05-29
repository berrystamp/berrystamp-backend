package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.OrderDto;
import com.berryinkstamp.berrybackendservice.dtos.request.PrintRequestDto;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order createNewOrder(OrderDto orderDto);
    Page<Order> fetchAllOrders(Pageable pageable);
    void orderDecision(Long orderId, Boolean decision);
    Page<Order> fetchAllLoggedInCustomerOrders(Pageable pageable);
    OrderRequest createNewOrderRequest(PrintRequestDto printRequestDto);
}
