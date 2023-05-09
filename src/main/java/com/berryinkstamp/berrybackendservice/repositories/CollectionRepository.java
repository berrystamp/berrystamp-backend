package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.Collection;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Optional<Collection> findCollectionByDesignerProfileIdAndName(@Param("designerId") Long designerId, @Param("name") String name);
    Page<Collection> findCollectionsByDesignerProfileId(Long designerId, Pageable pageable);
    Optional<Collection> findByIdAndDesignerProfile(Long id, Profile designerProfile);
    boolean existsByNameAndDesignerProfileAndIdNot(String name, Profile designerProfile, Long id);

}
