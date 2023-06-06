package com.berryinkstamp.berrybackendservice.controllers.publicAPI;

import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
        return designService.fetchDesignById(designId);
    }

    @Operation(summary = "Fetch design by slug", description = "Fetch design by slug")
    @GetMapping("/slug/{slug}")
    private Design getDesignBySlug(@PathVariable String slug){
        return designService.fetchDesignBySlug(slug);
    }

    @Operation(summary = "Fetch all designs", description = "Fetch all designs")
    @GetMapping()
    private Page<Design> fetchAllDesigns(@RequestParam(value = "designer", required = false) Long designerId,
                                         @RequestParam(value = "tag", required = false) String tag,
                                         @RequestParam(value = "category", required = false) String category,
                                         Pageable pageable){
        return designService.fetchAllDesign(designerId, tag,category,pageable);
    }

}
