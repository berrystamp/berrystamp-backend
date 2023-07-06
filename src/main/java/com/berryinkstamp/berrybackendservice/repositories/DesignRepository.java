package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {
    Optional<Design> findDesignByDesignerIdAndNameAndDeletedFalse(Long profileId, String name);
    List<Design> findAllByDesignerAndDeletedFalse(Profile designer, Pageable pageable);
    Optional<Design> findDesignBySlugAndDeletedFalseAndApprovedIsTrue(String name);
    Optional<Design> findByIdAndDesignerAndDeletedFalse(Long id, Profile designer);

    Optional<Design> findByIdAndDeletedFalse(Long id);
    Optional<Design> findByIdAndDeletedFalseAndApprovedIsTrue(Long id);

    Page<Design> findByDeletedAndApprovedIsTrue(boolean deleted, Pageable pageable);

    boolean existsByNameAndDesignerAndIdNot(String name, Profile designer, Long id);

    Page<Design> findByDeletedFalse(Pageable pageable);

    Page<Design> findByDeletedFalseAndStatus(DesignStatus status, Pageable pageable);



}
