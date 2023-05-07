package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.CollectionAdditionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.NewCollectionRequest;
import com.berryinkstamp.berrybackendservice.models.Collection;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/v1/api/collections")
public class CollectionController {
    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }


    @PostMapping()
    private Collection createNewCollection(@Valid @RequestBody NewCollectionRequest collectionRequest){
        return collectionService.createNewCollection(collectionRequest);
    }

    @PutMapping("/{collectionId}")
    private Collection updateCollection(@PathVariable Long collectionId, @Valid @RequestBody NewCollectionRequest collectionRequest){
        return collectionService.updateCollection(collectionId, collectionRequest);
    }

    @DeleteMapping("/{collectionId}")
    private String deleteCollection(@PathVariable Long collectionId){
        collectionService.deleteCollection(collectionId);
        return "Collection deleted";
    }

    @GetMapping("/{collectionId}")
    private Collection getCollectionById(@PathVariable Long collectionId){
        var collection = collectionService.fetchCollectionById(collectionId);

        return collection;
    }

    @PutMapping("/collection/{collectionId}/designs")
    private Object AddDesignToCollection(@PathVariable Long collectionId,
                                         @RequestBody CollectionAdditionRequest collectionAdditionRequest){
        return collectionService.addDesignsToCollection(collectionId, collectionAdditionRequest);
    }

    @GetMapping()
    private Page<Collection> fetchDesignerCollection(Pageable pageable){
        return collectionService.fetchAllDesignerCollections(pageable);
    }
}
