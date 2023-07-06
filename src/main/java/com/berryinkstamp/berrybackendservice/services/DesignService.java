package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.MockImagesDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateDesignRequest;
import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.MockImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface DesignService {

    Design createDesign(NewDesignRequest designRequest);

    Design updateDesign(Long id, UpdateDesignRequest designRequest);

    Map<Object,Object> deleteMock(Long mockId, Long designId);

    MockImages addMock(MockImagesDto dto, Long designId);

    Map<Object,Object> deleteDesign(Long designId);

    Design publicFetchDesignById(Long designId);

    Design fetchDesignById(Long designId, ProfileType profileType);

    Page<Design> fetchAllDesignsForDesigner(Pageable pageable);

    Design publicFetchDesignBySlug(String slug);

    Design acceptDesign(Long designId, boolean approved);

    Page<Design> adminFetchAllDesign(DesignStatus status, Pageable pageable);

    Design designerGetDesignById(Long designId);

    Design likeAndUnlikeDesign(Long designId, ProfileType profileType);

    Page<Design> fetchAllLikedDesign(ProfileType profileType, Pageable pageable);

    Page<Design> fetchAllDesign(Long designerId, String tags, String designCategories, String mocks, Integer upperPriceRange, Integer lowerPriceRange, String searchField, ProfileType profileType, Pageable pageable);

    Page<Design> publicFetchAllDesign(Long designerId, String tags, String designCategories, String mocks, Integer upperPriceRange, Integer lowerPriceRange, String searchField, Pageable pageable);
}
