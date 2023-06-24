package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.OrderDto;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderPaymentDto;
import com.berryinkstamp.berrybackendservice.dtos.request.PrintRequestDto;
import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface OrderService {
    Order createNewOrder(OrderDto orderDto);
    Page<Order> fetchAllOrders(OrderStatus orderStatus,Pageable pageable);
    void orderDecision(Long orderId, Boolean decision);
    Page<Order> fetchAllLoggedInCustomerOrders(ProfileType profileType, Pageable pageable);
    OrderRequest createNewOrderRequest(PrintRequestDto printRequestDto);
    Order fetchOrderById(Long orderId);
    Map<String,String> payForOrder(OrderPaymentDto orderPaymentDto);
}
