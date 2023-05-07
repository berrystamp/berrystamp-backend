package com.berryinkstamp.berrybackendservice.repositories;

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
public interface DesignRepository extends JpaRepository<Design, Long> {
    Optional<Design> findDesignByDesignerIdAndName(Long profileId, String name);
    List<Design>findAllByDesigner(Profile designer, Pageable pageable);
    Optional<Design> findDesignBySlug(String name);
    Page<Design> findByCollectionIdOrDesignerIdOrTagContainingIgnoreCaseOrCategoryContainingIgnoreCase(Long collectionId,
                                                                                               Long designerId,
                                                                                               String tag,
                                                                                               String category,
                                                                                               Pageable pageable);


}
