package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber,Long> {

    Optional<Subscriber> findAllByIdIgnoreCase(Long id);
}
