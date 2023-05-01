package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.OTPMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPMapperRepository extends JpaRepository<OTPMapper, Long> {
    Optional<OTPMapper> findFirstByUserId(Long userId);

}