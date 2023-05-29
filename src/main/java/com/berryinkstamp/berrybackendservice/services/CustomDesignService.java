package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.dtos.request.RequestCustomizationDTo;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;

public interface CustomDesignService {
    OrderRequest createCustomDesignRequest(CustomDesignRequestDto customDesignRequest);
    OrderRequest createCustomizedDesign(RequestCustomizationDTo designId);
}
