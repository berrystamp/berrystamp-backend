package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.CustomDesign;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDesignRepository extends JpaRepository<CustomDesign, Long> {
    Optional<CustomDesign> findCustomDesignByOrderId(Long orderId);
    List<CustomDesign> findCustomDesignByCustomerProfileAndIsCompletedIsTrue(Profile profile);
}
