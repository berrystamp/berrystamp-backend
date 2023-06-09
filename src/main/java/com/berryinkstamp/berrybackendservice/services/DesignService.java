package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.MockImagesDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateDesignRequest;
import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.MockImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DesignService {
    Design createDesign(NewDesignRequest designRequest);

    Design updateDesign(Long id, UpdateDesignRequest designRequest);

    Map<Object,Object> deleteMock(Long mockId, Long designId);

    MockImages addMock(MockImagesDto dto, Long designId);

    Map<Object,Object> deleteDesign(Long designId);
    Design fetchDesignById(Long designId);
    Page<Design> fetchAllDesignsForDesigner(Pageable pageable);
    Design fetchDesignBySlug(String slug);
    Page<Design> fetchAllDesign(Long designerId, String tag, String category, Pageable pageable);
    Design acceptDesign(Long designId, boolean approved);
    Page<Design> fetchAllDesign(DesignStatus status, Pageable pageable);
}
