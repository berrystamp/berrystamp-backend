package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_DESIGNER')")
@RestController
@RequestMapping("/v1/api/designs")
public class DesignController {

    private final DesignService designService;

    public DesignController(DesignService designService) {
        this.designService = designService;
    }

    @Operation(summary = "Create Design", description = "Create Design")
    @PostMapping()
    private Design createDesignController(@Valid @RequestBody NewDesignRequest designRequest){
        return designService.createDesign(designRequest);
    }

    @Operation(summary = "Delete Design", description = "Delete Design")
    @DeleteMapping("/{designId}")
    private Map<Object,Object> deleteDesign(@PathVariable Long designId){
       return designService.deleteDesign(designId);
    }

    @Operation(summary = "Fetch all Designs By Designer", description = "Fetch all Designs by designer")
    @GetMapping()
    private Page<Design> fetchAllDesignForDesigner(Pageable pageable){
        return designService.fetchAllDesignsForDesigner(pageable);
    }



}
