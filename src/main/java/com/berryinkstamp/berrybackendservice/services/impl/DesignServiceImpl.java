package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.DesignRepository;
import com.berryinkstamp.berrybackendservice.repositories.MockImageRepository;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import com.berryinkstamp.berrybackendservice.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DesignServiceImpl implements DesignService {
    private final DesignRepository designRepository;

    private final MockImageRepository mockImageRepository;
    private final TokenProvider tokenProvider;

    public DesignServiceImpl(DesignRepository designRepository,
                             MockImageRepository mockImageRepository,
                             TokenProvider tokenProvider) {
        this.designRepository = designRepository;
        this.mockImageRepository = mockImageRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Design createDesign(NewDesignRequest designRequest) {

       var design = Optional.of(tokenProvider.getCurrentUser())
                .filter(user -> !checkIfDesignNameExistForUser(designRequest.getName(), user.getDesignerProfile().getId()))
                .map(user -> mapDesignRequestDtoToEntity(designRequest, user))
                .map(designRepository::save)
                .orElseThrow(() -> new BadRequestException("Name already exist for this design!"));
        //TODO: push to audit
        return design;
    }

    @Override
    public Map<Object,Object> deleteDesign(Long designId) {
        designRepository.deleteById(designId);
        //TODO: push to audit
        return new HashMap<>();
    }

    @Override
    public Design fetchDesignById(Long designId) {
        var design = designRepository.findById(designId)
                .orElseThrow(()-> new NotFoundException("No design with such Id found!"));
          //TODO: push To Audit
        return design;
    }

    @Override
    public List<Design> fetchAllDesignsForDesigner(Pageable pageable) {
        List<Design>designs = Optional.of(tokenProvider.getCurrentUser())
                .map(User::getDesignerProfile)
                .map(profile -> designRepository.findAllByDesigner(profile, pageable))
                .orElseThrow(() -> new NotFoundException("No design found for designer!"));
        //TODO: push to audit
        return designs;
    }

    @Override
    public Design fetchDesignBySlug(String slug) {
        var design = designRepository.findDesignBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Design not found!"));

        //TODO: push To audit

        return design;
    }

    @Override
    public Page<Design> fetchAllDesign(Long collectionId, Long designerId, String tag, String category, Pageable pageable) {
        if(collectionId == null && designerId == null && tag == null && category == null){
            return designRepository.findAll(pageable);
        }
       return designRepository
               .findByCollectionIdOrDesignerIdOrTagContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                       collectionId,
                       designerId,
                       tag,
                       category,
                       pageable
               );
    }

    private Boolean checkIfDesignNameExistForUser(String designName, Long designerProfileId){
        Optional<Design>design = designRepository.findDesignByDesignerIdAndName(designerProfileId, designName);
        return design.isPresent();
    }

    private Design mapDesignRequestDtoToEntity(NewDesignRequest designRequest, User user){
        var design = new Design();
        var mocks = mockImageRepository.saveAll(designRequest.getMocks());
        design.setAmount(designRequest.getAmount());
        design.setDescription(designRequest.getDescription());
        design.setCategory(designRequest.getCategory().trim());
        design.setMocks(mocks);
        design.setPrinter(designRequest.getPrinterId());
        design.setDesigner(user.getDesignerProfile());
        design.setImageUrlBack(designRequest.getBackImageUrl());
        design.setImageUrlFront(designRequest.getFrontImageUrl());
        design.setName(designRequest.getName());
        design.setTag(designRequest.getTags().trim());
        design.setSlug(Utils.generateSlug(designRequest.getName()));

        return design;
    }

}
