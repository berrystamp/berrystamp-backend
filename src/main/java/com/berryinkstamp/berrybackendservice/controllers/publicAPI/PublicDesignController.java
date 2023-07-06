package com.berryinkstamp.berrybackendservice.controllers.publicAPI;

import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/designs")
public class PublicDesignController {

    private final DesignService designService;

    public PublicDesignController(DesignService designService) {
        this.designService = designService;
    }

    @Operation(summary = "Fetch design by Id", description = "Fetch design by Id")
    @GetMapping("/{designId}")
    private Design fetchDesignById(@PathVariable Long designId){
        return designService.publicFetchDesignById(designId);
    }

    @Operation(summary = "Fetch design by slug", description = "Fetch design by slug")
    @GetMapping("/slug/{slug}")
    private Design getDesignBySlug(@PathVariable String slug){
        return designService.publicFetchDesignBySlug(slug);
    }

    @Operation(summary = "Fetch all designs", description = "Fetch all designs")
    @GetMapping()
    private Page<Design> fetchAllDesigns(@RequestParam(value = "designer", required = false) Long designerId,
                                         @RequestParam(value = "tags", required = false) String tags,
                                         @RequestParam(value = "designCategories", required = false) String designCategories,
                                         @RequestParam(value = "mocks", required = false) String mocks,
                                         @RequestParam(value = "upperPriceRange", required = false) Integer upperPriceRange,
                                         @RequestParam(value = "lowerPriceRange", required = false) Integer lowerPriceRange,
                                         @RequestParam(value = "searchField", required = false) String searchField,
                                         @PageableDefault()
                                             @SortDefault.SortDefaults({
                                                     @SortDefault(sort = "amount", direction = Sort.Direction.ASC),
                                                     @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC)
                                             }) Pageable pageable){
        return designService.publicFetchAllDesign(designerId, tags,designCategories, mocks, upperPriceRange, lowerPriceRange, searchField, pageable);
    }

}
