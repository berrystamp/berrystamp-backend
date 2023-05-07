package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.CollectionAdditionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.NewCollectionRequest;
import com.berryinkstamp.berrybackendservice.models.Collection;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/collections")
@WrapApiResponse
public class PublicCollectionController {
    private final DesignService designService;
    private final CollectionService collectionService;

    public PublicCollectionController(DesignService designService, CollectionService collectionService) {
        this.designService = designService;
        this.collectionService = collectionService;
    }


    @GetMapping("/{collectionId}")
    private Collection getCollectionById(@PathVariable Long collectionId){
        var collection = collectionService.fetchCollectionById(collectionId);

        return collection;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    private Page<Collection> fetchAllCollections(@RequestParam(value = "designerId", required = false) Long designerId,
                                                 Pageable pageable){
        return collectionService.fetchAllCollection(designerId, pageable);
    }
}
