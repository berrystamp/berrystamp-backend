package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.Follow;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findFirstByFollowerAndFollowing(Profile follower, Profile following);

    List<Follow> findByFollowing(Profile following);

    List<Follow> findByFollower(Profile follower);


}