package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ROLE_DESIGNER')")
@RestController
@RequestMapping("/v1/api/designs")
public class DesignController {
    private final DesignService designService;

    public DesignController(DesignService designService) {
        this.designService = designService;
    }
    @PostMapping()
    private Design createDesignController(@Valid @RequestBody NewDesignRequest designRequest){
        var design = designService.createDesign(designRequest);
        return design;
    }
    @DeleteMapping("/{designId}")
    private Map<Object,Object> deleteDesign(@PathVariable Long designId){
       return designService.deleteDesign(designId);
    }

    @GetMapping()
    private List<Design> fetchAllDesignForDesigner(Pageable pageable){
        return designService.fetchAllDesignsForDesigner(pageable);
    }



}
