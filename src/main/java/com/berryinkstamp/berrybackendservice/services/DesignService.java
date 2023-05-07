package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.models.Design;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DesignService {
    Design createDesign(NewDesignRequest designRequest);
    Map<Object,Object> deleteDesign(Long designId);
    Design fetchDesignById(Long designId);
    List<Design>fetchAllDesignsForDesigner(Pageable pageable);
    Design fetchDesignBySlug(String slug);
    Page<Design> fetchAllDesign(Long collectionId, Long designerId, String tag, String category, Pageable pageable);
}
