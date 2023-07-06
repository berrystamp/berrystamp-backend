package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.DesignLike;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignLikeRepository extends JpaRepository<DesignLike, Long> {
    Optional<DesignLike> findFirstByDesignAndProfile(Design design, Profile profile);

    List<DesignLike> findByProfile(Profile profile);




}