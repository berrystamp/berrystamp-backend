package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.models.CustomDesignRequest;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;

public interface CustomDesignService {
    OrderRequest uploadCustomDesign(CustomDesignRequestDto customDesignRequest);
    OrderRequest createCustomizedDesign(Long designId);
}
