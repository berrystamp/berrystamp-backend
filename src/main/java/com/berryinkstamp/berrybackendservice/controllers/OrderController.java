package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderDto;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderRequestCreation;
import com.berryinkstamp.berrybackendservice.dtos.request.PrintRequestDto;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import com.berryinkstamp.berrybackendservice.models.PrintRequest;
import com.berryinkstamp.berrybackendservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@WrapApiResponse
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;

    @Operation(summary = "create a new order for designer or printer")
    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_DESIGNER', 'ROLE_PRINTER')")
    public Order createNewOrder(@Valid OrderDto orderDto){
     return orderService.createNewOrder(orderDto);
    }

    @Operation(summary = "fetch all orders present")
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Order> fetchAllOrders(Pageable pageable){
        return orderService.fetchAllOrders(pageable);
    }

    @Operation(summary = "Accept or Decline order")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Map<Object,Object>acceptOrDeclineOrder(Long orderId, Boolean status){
        orderService.orderDecision(orderId, status);
        return new HashMap<>();
    }

    @Operation(summary = "Fetch all orders for logged In customer profile")
    @GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Page<Order> fetchAllOrdersForCustomer(Pageable pageable){
        return orderService.fetchAllLoggedInCustomerOrders(pageable);
    }

    @Operation(summary = "Create a order request for type Print")
    @PostMapping("/{designId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderRequest createNewPrintRequest(@Valid @RequestBody PrintRequestDto printRequestDto, @PathVariable Long designId){
        return orderService.createNewOrderRequest(printRequestDto, designId);
    }

}
