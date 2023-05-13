package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByProfileTypeAndNameAllIgnoreCase(ProfileType profileType, String businessName);

    Page<Profile> findByProfileType(ProfileType profileType, Pageable pageable);




}