package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.models.Collection;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/collections")
@WrapApiResponse
@RequiredArgsConstructor
public class PublicCollectionController {

    private final CollectionService collectionService;


    @Operation(summary = "Get Collection By Id", description = "Get Collection By Id")
    @GetMapping("/{collectionId}")
    private Collection getCollectionById(@PathVariable Long collectionId){
        return collectionService.fetchCollectionById(collectionId);
    }

    @Operation(summary = "Fetch all collections", description = "Fetch all collections")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    private Page<Collection> fetchAllCollections(@RequestParam(value = "designerId", required = false) Long designerId,
                                                 Pageable pageable){
        return collectionService.fetchAllCollection(designerId, pageable);
    }
}
