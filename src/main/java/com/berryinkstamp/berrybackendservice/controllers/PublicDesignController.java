package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/designs")
public class PublicDesignController {
    private final DesignService designService;
    private final CollectionService collectionService;

    public PublicDesignController(DesignService designService, CollectionService collectionService) {
        this.designService = designService;
        this.collectionService = collectionService;
    }

    @GetMapping("/{designId}")
    private Design fetchDesignById(@PathVariable Long designId){
        return designService.fetchDesignById(designId);
    }

    @GetMapping("/slug/{slug}")
    private Design getDesignBySlug(@PathVariable String slug){
        var design = designService.fetchDesignBySlug(slug);
        return design;
    }

    @GetMapping()
    private Page<Design> fetchAllDesigns(@RequestParam(value = "collection", required = false) Long collectionId,
                                         @RequestParam(value = "designer", required = false) Long designerId,
                                         @RequestParam(value = "tag", required = false) String tag,
                                         @RequestParam(value = "category", required = false) String category,
                                         Pageable pageable){
        return designService.fetchAllDesign(collectionId,designerId, tag,category,pageable);
    }

}
