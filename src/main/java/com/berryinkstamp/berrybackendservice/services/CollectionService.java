package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.CollectionAdditionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.NewCollectionRequest;
import com.berryinkstamp.berrybackendservice.models.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionService {
    Collection createNewCollection(NewCollectionRequest collectionRequest);
    Collection updateCollection(Long id, NewCollectionRequest collectionRequest);
    void deleteCollection(Long id);
    Collection fetchCollectionById(Long id);
    Collection addDesignsToCollection(Long id, CollectionAdditionRequest collectionRequest);
    Page<Collection> fetchAllCollection(Long designerId, Pageable pageable);
    Page<Collection> fetchAllDesignerCollections(Pageable pageable);
}
