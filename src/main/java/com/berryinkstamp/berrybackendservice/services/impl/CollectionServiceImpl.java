package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.CollectionAdditionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.NewCollectionRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Collection;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.CollectionRepository;
import com.berryinkstamp.berrybackendservice.repositories.DesignRepository;
import com.berryinkstamp.berrybackendservice.services.CollectionService;
import com.berryinkstamp.berrybackendservice.services.UserService;
import com.berryinkstamp.berrybackendservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final DesignRepository designRepository;
    private final TokenProvider tokenProvider;

    public CollectionServiceImpl(CollectionRepository collectionRepository,
                                 DesignRepository designRepository, TokenProvider tokenProvider) {
        this.collectionRepository = collectionRepository;
        this.designRepository = designRepository;
        this.tokenProvider = tokenProvider;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionServiceImpl.class);

    @Override
    public Collection createNewCollection(NewCollectionRequest collectionRequest) {
        var collection = Optional.of(tokenProvider.getCurrentUser())
                .map(User::getDesignerProfile)
                .filter(profile -> checkIfProfileExistForCollection(profile,collectionRequest))
                .map(profile -> mapCollectionRequestToEntity(collectionRequest, profile))
                .orElseThrow(()-> new BadRequestException("Collection with same name already exist!") );
        //TODO: push to audit
        return collectionRepository.save(collection);
    }

    @Override
    public Collection updateCollection(Long id, NewCollectionRequest collectionRequest) {
        Collection collection = collectionRepository.findById(id)
                .map(collection1 -> updateCollectionEntity(collectionRequest, collection1))
                .map(collection1 -> collectionRepository.save(collection1))
                .orElseThrow(() -> new NotFoundException("Collection not found!"));

        //TODO: push to audit
        return collection;
    }

    @Override
    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
        //TODO: push to audit
    }

    @Override
    public Collection fetchCollectionById(Long id) {
        var collection = collectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Collection not found"));
        //TODO: push To Audit

        return collection;
    }

    @Override
    public Collection addDesignsToCollection(Long collectionId, CollectionAdditionRequest collectionRequest) {
        var designs = collectionRequest.getDesigns()
                .stream()
                .map(id -> designRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Design with id not found")))
                .toList();

        var collection = collectionRepository.findById(collectionId)
                .map(collection1 -> {
                    collection1.getDesigns().addAll(designs);
                    return collectionRepository.save(collection1);
                }).orElseThrow(() -> new NotFoundException("Collection with id not found"));

        return collection;
    }

    @Override
    public Page<Collection> fetchAllCollection(Long designerId, Pageable pageable) {
        return Optional.ofNullable(designerId)
                .map(id -> collectionRepository.findCollectionsByDesignerProfileId(id, pageable))
                .orElseGet(() ->collectionRepository.findAll(pageable));
    }

    @Override
    public Page<Collection> fetchAllDesignerCollections(Pageable pageable) {
      Long designerId = tokenProvider.getCurrentUser().getDesignerProfile().getId();
      return collectionRepository.findCollectionsByDesignerProfileId(designerId,pageable);
    }

    private Collection mapCollectionRequestToEntity(NewCollectionRequest collectionRequest, Profile designer){
        var collection = new Collection();
        collection.setPicture(collection.getPicture());
        collection.setDesignerProfile(designer);
        collection.setDescription(collectionRequest.getDescription());
        collection.setSlug(Utils.generateSlug(collectionRequest.getName()));
        collection.setName(collectionRequest.getName());
        return collection;
    }

    private Collection updateCollectionEntity(NewCollectionRequest collectionRequest, Collection collection){
        collection.setDescription(collection.getDescription());
        collection.setPicture(collection.getPicture());
        collection.setSlug(Utils.generateSlug(collectionRequest.getName()));
        collection.setName(collectionRequest.getName());

        return collection;
    }

    private Boolean checkIfProfileExistForCollection(Profile designer, NewCollectionRequest collectionRequest){
        var collection  = collectionRepository.findCollectionByDesignerProfileIdAndNameOrSlug(
                designer.getId(),
                collectionRequest.getName(),
                Utils.generateSlug(collectionRequest.getName()));

        return collection.isEmpty();
    }

}
