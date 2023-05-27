package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {
}
