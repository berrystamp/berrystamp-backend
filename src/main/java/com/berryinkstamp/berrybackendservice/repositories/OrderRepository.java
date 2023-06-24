package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order>findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
    Optional<Order>findOrderByOrderRequest_CustomerProfileAndId(Profile profile, Long orderId);
    Optional<Order>findOrderByTransactionRef(String ref);
    Page<Order>findAllByOrderRequest_CustomerProfile(Profile profile,Pageable pageable);
}
