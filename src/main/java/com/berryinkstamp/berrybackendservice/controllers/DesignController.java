package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.MockImagesDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateDesignRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.MockImages;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/v1/api/designs")
public class DesignController {

    private final DesignService designService;

    public DesignController(DesignService designService) {
        this.designService = designService;
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "Create Design", description = "Create Design")
    @PostMapping()
    private Design createDesign(@Valid @RequestBody NewDesignRequest designRequest){
        return designService.createDesign(designRequest);
    }


    @Operation(summary = "Get all Designs and filter - with like option", description = "Fetch all designs")
    @GetMapping()
    private Page<Design> fetchAllApprovedDesigns(@RequestParam(value = "designer", required = false) Long designerId,
                                         @RequestParam(value = "tags", required = false) String tags,
                                         @RequestParam(value = "designCategories", required = false) String designCategories,
                                         @RequestParam(value = "mocks", required = false) String mocks,
                                         @RequestParam(value = "upperPriceRange", required = false) Integer upperPriceRange,
                                         @RequestParam(value = "lowerPriceRange", required = false) Integer lowerPriceRange,
                                         @RequestParam(value = "searchField", required = false) String searchField,
                                         @RequestHeader(value = "profileType") ProfileType profileType,
                                                 @PageableDefault(size = 10 )
                                                     @SortDefault.SortDefaults({
                                                         @SortDefault(sort = "amount", direction = Sort.Direction.ASC),
                                                         @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC)
                                                 }) Pageable pageable){
        return designService.fetchAllDesign(designerId, tags, designCategories, mocks, upperPriceRange, lowerPriceRange, searchField, profileType, pageable);
    }

    @Operation(summary = "Fetch design by Id - with like option", description = "Fetch design by Id")
    @GetMapping("/{designId}")
    private Design fetchApprovedDesignById(@PathVariable Long designId, @RequestHeader(value = "profileType") ProfileType profileType){
        return designService.fetchDesignById(designId, profileType);
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "Update Design", description = "Update Design")
    @PutMapping("/{designId}")
    private Design updateDesign(@Valid @RequestBody UpdateDesignRequest designRequest, @PathVariable Long designId){
        return designService.updateDesign(designId, designRequest);
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "Designer Get Design by Id - returns design whether approved or not", description = "Get Design by Id")
    @GetMapping("/{designId}/designer")
    private Design designerGetDesignById(@PathVariable Long designId){
        return designService.designerGetDesignById(designId);
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "Add mock to design", description = "Add mock to design")
    @PostMapping("/{designId}/mock")
    private MockImages addMockToDesign(@Valid @RequestBody MockImagesDto designRequest, @PathVariable Long designId){
        return designService.addMock(designRequest, designId);
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "remove mock to design", description = "remove mock to design")
    @DeleteMapping("/{designId}/mock/{mockId}")
    private Map<Object,Object> removeMockToDesign(@PathVariable Long designId, @PathVariable Long mockId){
        return designService.deleteMock(mockId, designId);
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "Delete Design", description = "Delete Design")
    @DeleteMapping("/{designId}")
    private Map<Object,Object> deleteDesign(@PathVariable Long designId){
       return designService.deleteDesign(designId);
    }

    @PreAuthorize("hasRole('ROLE_DESIGNER')")
    @Operation(summary = "Fetch all Designs By Designer", description = "Fetch all Designs by designer")
    @GetMapping("/all/designer")
    private Page<Design> fetchAllDesignForDesigner(Pageable pageable){
        return designService.fetchAllDesignsForDesigner(pageable);
    }


    @Operation(summary = "Like and Unlike Design", description = "Like and Unlike Design")
    @PatchMapping("/{designId}/likes")
    private Design likeAndUnlikeDesign(@PathVariable Long designId, @RequestHeader(value = "profileType") ProfileType profileType){
        return designService.likeAndUnlikeDesign(designId, profileType);
    }

    @Operation(summary = "Like and Unlike Design", description = "Like and Unlike Design")
    @GetMapping("/all/likes")
    private Page<Design> fetchAllLikedDesign(@RequestHeader(value = "profileType") ProfileType profileType, Pageable pageable){
        return designService.fetchAllLikedDesign(profileType, pageable);
    }


}
