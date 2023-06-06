package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/orders")
public class AdminOrderController {

    private OrderService orderService;

    @Operation(summary = "fetch all orders present")
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Order> fetchAllOrders(@RequestParam(required = false) OrderStatus orderStatus, Pageable pageable){
        return orderService.fetchAllOrders(orderStatus, pageable);
    }
}
