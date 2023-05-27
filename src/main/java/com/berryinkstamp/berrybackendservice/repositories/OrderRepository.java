package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order>findAllByCustomerProfile(Profile customerProfile, Pageable pageable);
}
