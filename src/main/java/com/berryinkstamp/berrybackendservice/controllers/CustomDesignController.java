package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RequestCustomizationDTo;
import com.berryinkstamp.berrybackendservice.models.CustomDesign;
import com.berryinkstamp.berrybackendservice.models.CustomDesignRequest;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import com.berryinkstamp.berrybackendservice.services.CustomDesignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@WrapApiResponse
@RequestMapping("/api/v1/custom-designs")
@AllArgsConstructor
public class CustomDesignController {
    private CustomDesignService customDesignService;

    @Operation(summary = "Fetch all completed custom design for customer")
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public List<CustomDesign> fetchCompletedCustomDesigns(){
        return customDesignService.fetchAllCompletedCustomDesign();
    }

    @Operation(summary = "create new custom design request")
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderRequest createCustomDesignRequest(@Valid @RequestBody CustomDesignRequestDto customDesignRequest){
        return customDesignService.createCustomDesignRequest(customDesignRequest);
    }

    @Operation(summary = "request customization for design")
    @PostMapping("/customization")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderRequest requestCustomization(@Valid @RequestBody RequestCustomizationDTo customDesignRequest){
        return customDesignService.createCustomizedDesign(customDesignRequest);
    }

    @Operation(summary = "Fetch custom design by Order Id")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_DESIGNER')")
    public CustomDesign fetchCustomDesignByOrderId(@PathVariable Long orderId){
        return customDesignService.fetchCustomDesignByOrderId(orderId);
    }
}
