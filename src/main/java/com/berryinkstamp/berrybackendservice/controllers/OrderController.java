package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.*;
import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.CustomDesign;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import com.berryinkstamp.berrybackendservice.models.PrintRequest;
import com.berryinkstamp.berrybackendservice.services.CustomDesignService;
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
    private final OrderService orderService;
    private final CustomDesignService customDesignService;

    @Operation(summary = "create a new order for designer or printer")
    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_DESIGNER', 'ROLE_PRINTER')")
    public Order createNewOrder(@Valid @RequestBody OrderDto orderDto){
     return orderService.createNewOrder(orderDto);
    }

    @Operation(summary = "Accept or Decline order")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Map<Object,Object>acceptOrDeclineOrder(Long orderId, Boolean status){
        orderService.orderDecision(orderId, status);
        return new HashMap<>();
    }

    @Operation(summary = "Fetch all orders for logged In customer profile")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Page<Order> fetchAllOrdersForCustomer(@RequestHeader(value = "profile_type") ProfileType profileType, Pageable pageable){
        return orderService.fetchAllLoggedInCustomerOrders(profileType, pageable);
    }

    @Operation(summary = "Create a order request for type Print")
    @PostMapping("/order-request")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderRequest createNewPrintRequest(@Valid @RequestBody PrintRequestDto printRequestDto){
        return orderService.createNewOrderRequest(printRequestDto);
    }

    @Operation(summary = "Fetch order by id")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_DESIGNER')")
    public Order fetchOrderById(@PathVariable Long orderId){
        return orderService.fetchOrderById(orderId);
    }


    @Operation(summary = "create custom designs")
    @PutMapping("/{orderId}/custom-design")
    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    public CustomDesign uploadCustomDesign(@Valid @RequestBody NewDesignRequest newDesignRequest, @PathVariable Long orderId){
        return customDesignService.uploadCustomDesign(newDesignRequest, orderId);
    }

    @Operation(summary = "pay for order")
    @PutMapping("/{orderId}/")
    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    public Map<String, String> uploadCustomDesign(@Valid @RequestBody OrderPaymentDto orderPaymentDto){
        return orderService.payForOrder(orderPaymentDto);
    }



}
