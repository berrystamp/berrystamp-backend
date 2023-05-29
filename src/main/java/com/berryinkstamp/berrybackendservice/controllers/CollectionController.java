package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.CollectionAdditionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.CollectionMoveRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.NewCollectionRequest;
import com.berryinkstamp.berrybackendservice.models.Collection;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_DESIGNER')")
@RestController
@RequestMapping("/v1/api/collections")
public class CollectionController {
    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }


    @Operation(summary = "Create Collection", description = "Create Collection")
    @PostMapping()
    private Collection createNewCollection(@Valid @RequestBody NewCollectionRequest collectionRequest){
        return collectionService.createNewCollection(collectionRequest);
    }

    @Operation(summary = "Update Collection", description = "Update Collection")
    @PutMapping("/{collectionId}")
    private Collection updateCollection(@PathVariable Long collectionId, @Valid @RequestBody NewCollectionRequest collectionRequest){
        return collectionService.updateCollection(collectionId, collectionRequest);
    }

    @Operation(summary = "Delete Collection", description = "Delete Collection")
    @DeleteMapping("/{collectionId}")
    private Object deleteCollection(@PathVariable Long collectionId){
        collectionService.deleteCollection(collectionId);
        return Map.of();
    }

    @Operation(summary = "Add Design to Collection", description = "Add Design to Collection")
    @PutMapping("/{collectionId}/designs/add")
    private Object addDesignToCollection(@PathVariable Long collectionId,
                                         @RequestBody CollectionAdditionRequest collectionAdditionRequest){
        return collectionService.addDesignsToCollection(collectionId, collectionAdditionRequest);
    }

    @Operation(summary = "Move / Remove(newCollection == null) designs from Collection", description = "Move / Remove(newCollection == null) designs from Collection")
    @PutMapping("/{collectionId}/designs/move")
    private Collection moveDesignToCollection(@PathVariable Long collectionId,
                                         @RequestBody CollectionMoveRequest collectionMoveRequest){
        return collectionService.moveDesignToCollection(collectionId, collectionMoveRequest);
    }

    @Operation(summary = "Fetch all Collections By Designer", description = "Fetch all Collections By Designer")
    @GetMapping()
    private Page<Collection> fetchDesignerCollection(Pageable pageable){
        return collectionService.fetchAllDesignerCollections(pageable);
    }

    //todo create new endpoint for admin. it will be used to either decline aor accept a design.
    //todo admin should be able fetch all design and filter by status
    //todo admin fetch all profile and you should be able to filter by profile
}
