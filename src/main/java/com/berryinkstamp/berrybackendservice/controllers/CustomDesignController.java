package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.models.CustomDesignRequest;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import com.berryinkstamp.berrybackendservice.services.CustomDesignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@WrapApiResponse
@RequestMapping("/api/v1/custom-designs")
@AllArgsConstructor
public class CustomDesignController {
    private CustomDesignService customDesignService;

    @Operation(summary = "create new custom design")
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderRequest createCustomDesign(@Valid @RequestBody CustomDesignRequestDto customDesignRequest){
        return customDesignService.uploadCustomDesign(customDesignRequest);
    }

    @Operation(summary = "request customization for design")
    @PostMapping("/{designId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderRequest requestCustomization(@PathVariable Long designId){
        return customDesignService.createCustomizedDesign(designId);
    }
}
