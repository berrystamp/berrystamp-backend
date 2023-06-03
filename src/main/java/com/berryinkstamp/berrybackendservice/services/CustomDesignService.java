package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RequestCustomizationDTo;
import com.berryinkstamp.berrybackendservice.models.CustomDesign;
import com.berryinkstamp.berrybackendservice.models.CustomDesignRequest;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;

import java.util.List;

public interface CustomDesignService {
    OrderRequest createCustomDesignRequest(CustomDesignRequestDto customDesignRequest);
    OrderRequest createCustomizedDesign(RequestCustomizationDTo designId);
    CustomDesign uploadCustomDesign(NewDesignRequest designRequest, Long orderId);
    CustomDesign fetchCustomDesignByOrderId(Long orderId);
    List<CustomDesign>fetchAllCompletedCustomDesign();
}
