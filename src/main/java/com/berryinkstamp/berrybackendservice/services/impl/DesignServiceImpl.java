
package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.MockImagesDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateDesignRequest;
import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.MockImages;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.DesignRepository;
import com.berryinkstamp.berrybackendservice.repositories.MockImageRepository;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import com.berryinkstamp.berrybackendservice.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

        if (designNameExistForUser(designRequest.getName(), tokenProvider.getCurrentUser().getDesignerProfile().getId())) {
            throw new BadRequestException("Name already exist for this design!");
        }

        Design design = new Design();
        mapDesignRequestDtoToEntity(design, designRequest, tokenProvider.getCurrentUser());
        design = designRepository.save(design);
        Set<MockImages> mocks = createkMocks(design, designRequest.getMocks());
        design.setMocks(mocks);
        return design;
    }

    @Override
    public Design updateDesign(Long id, UpdateDesignRequest designRequest) {
        Optional<Design> optionalDesign = designRepository.findByIdAndDesignerAndDeletedFalse(id, tokenProvider.getCurrentUser().getDesignerProfile());
        if (optionalDesign.isEmpty()) {
            throw new NotFoundException("Design not found");
        }

        if (designNameExistForUser(optionalDesign.get(), tokenProvider.getCurrentUser().getDesignerProfile(), designRequest.getName())) {
            throw new BadRequestException("Name already exist for this design!");
        }
        Design design = mapDesignRequestDtoToEntity(optionalDesign.get(), designRequest, tokenProvider.getCurrentUser());
        design = designRepository.save(design);
        return design;
    }

    private Set<MockImages> createkMocks(Design design, List<MockImagesDto> mockImagesDtos) {
        HashSet<MockImages> mockImages = new HashSet<>();
        mockImagesDtos.forEach(a -> {
            MockImages mockImage = new MockImages();
            mockImage.setName(a.getName());
            mockImage.setAvailableQty(a.getAvailableQty());
            mockImage.setImageUrl(a.getImageUrl());
            mockImage.setLimitedStatus(a.getLimitedStatus());
            mockImage.setDesign(design);
            mockImages.add(mockImageRepository.save(mockImage));
        });
        return mockImages;
    }

    @Override
    public Map<Object,Object> deleteMock(Long mockId, Long designId) {
        Optional<Design> optionalDesign = designRepository.findByIdAndDesignerAndDeletedFalse(designId, tokenProvider.getCurrentUser().getDesignerProfile());
        if (optionalDesign.isEmpty()) {
            throw new NotFoundException("Design not found");
        }

        Optional<MockImages> mockImage = mockImageRepository.findByIdAndDesign(mockId, optionalDesign.get());
        if (mockImage.isEmpty()) {
            throw new NotFoundException("Mock not found");
        }

        mockImageRepository.delete(mockImage.get());
        //TODO: push to audit
        return new HashMap<>();
    }

    @Override
    public MockImages addMock(MockImagesDto dto, Long designId) {
        Optional<Design> optionalDesign = designRepository.findByIdAndDesignerAndDeletedFalse(designId, tokenProvider.getCurrentUser().getDesignerProfile());
        if (optionalDesign.isEmpty()) {
            throw new NotFoundException("Design not found");
        }

        MockImages mockImage = new MockImages();
        mockImage.setName(dto.getName());
        mockImage.setAvailableQty(dto.getAvailableQty());
        mockImage.setImageUrl(dto.getImageUrl());
        mockImage.setLimitedStatus(dto.getLimitedStatus());
        mockImage.setDesign(optionalDesign.get());

        mockImage = mockImageRepository.save(mockImage);
        //TODO: push to audit
        return mockImage;
    }

    @Override
    public Map<Object,Object> deleteDesign(Long designId) {
        Optional<Design> optionalDesign = designRepository.findByIdAndDesignerAndDeletedFalse(designId, tokenProvider.getCurrentUser().getDesignerProfile());
        if (optionalDesign.isEmpty()) {
            throw new NotFoundException("Design not found");
        }
        Design design = optionalDesign.get();
        design.setDeleted(true);
        designRepository.save(design);
        //TODO: push to audit
        return new HashMap<>();
    }

    @Override
    public Design fetchDesignById(Long designId) {
        var design = designRepository.findByIdAndDeletedFalse(designId)
                .orElseThrow(()-> new NotFoundException("No design with such Id found!"));
        //TODO: push To Audit
        return design;
    }

    @Override
    public Page<Design> fetchAllDesignsForDesigner(Pageable pageable) {
        List<Design>designs = Optional.of(tokenProvider.getCurrentUser())
                .map(User::getDesignerProfile)
                .map(profile -> designRepository.findAllByDesignerAndDeletedFalse(profile, pageable))
                .orElseThrow(() -> new NotFoundException("No design found for designer!"));
        //TODO: push to audit
        return new PageImpl<>(designs, pageable, designs.size());
    }

    @Override
    public Design fetchDesignBySlug(String slug) {
        var design = designRepository.findDesignBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new NotFoundException("Design not found!"));

        //TODO: push To audit

        return design;
    }

    @Override
    public Page<Design> fetchAllDesign(Long collectionId, Long designerId, String tag, String category, Pageable pageable) {
        if(collectionId == null && designerId == null && tag == null && category == null){
            return designRepository.findByDeleted(false, pageable);
        }

        //todo should fetch only designs that are approved
        return designRepository
                .findByCollectionIdOrDesignerIdOrTagContainingIgnoreCaseOrCategoryContainingIgnoreCaseAndDeletedFalse(
                        collectionId,
                        designerId,
                        tag,
                        category,
                        pageable
                );
    }

    @Override
    public void acceptDesign(Long designId) {
      Optional<Design> design = designRepository.findById(designId);
      if (design.isPresent()){
        Design design1 = design.get();
        design1.accept();
        designRepository.save(design1);
      }else {
        throw new NotFoundException("Design not found with this ID: " + designId);
        }
    }

    @Override
    public void declineDesign(Long designId) {
       Optional<Design> design = designRepository.findById(designId);
       if (design.isPresent()){
           Design design1 = design.get();
           design1.decline();
           designRepository.save(design1);
       }else {
           throw new NotFoundException("Design not found with this ID: " + designId);
       }
    }

    @Override
    public List<Design> fetchAllDesign() {
        return designRepository.findAll();
    }

    @Override
    public List<Design> fetchDesignByDesignStatus(DesignStatus designStatus) {
        return designRepository.findByDesignStatus(designStatus);
    }

    private Boolean designNameExistForUser(String designName, Long designerProfileId){
        Optional<Design>design = designRepository.findDesignByDesignerIdAndNameAndDeletedFalse(designerProfileId, designName);
        return design.isPresent();
    }

    private Boolean designNameExistForUser(Design design, Profile designer, String name){
        return designRepository.existsByNameAndDesignerAndIdNot(name, designer, design.getId());
    }

    private Design mapDesignRequestDtoToEntity(Design design, NewDesignRequest designRequest, User user){
        design.setAmount(designRequest.getAmount());
        design.setDescription(designRequest.getDescription());
        design.setCategory(String.join(",", designRequest.getCategory()));
        design.setPrinter(designRequest.getPrinterId());
        design.setDesigner(user.getDesignerProfile());
        design.setImageUrlBack(designRequest.getBackImageUrl());
        design.setImageUrlFront(designRequest.getFrontImageUrl());
        design.setName(designRequest.getName());
        design.setTag(String.join(",", designRequest.getTags()));
        design.setSlug(Utils.generateSlug(designRequest.getName()));

        return design;
    }

    private Design mapDesignRequestDtoToEntity(Design design, UpdateDesignRequest designRequest, User user){
        design.setAmount(designRequest.getAmount());
        design.setDescription(designRequest.getDescription());
        design.setCategory(String.join(",", designRequest.getCategory()));
        design.setPrinter(designRequest.getPrinterId());
        design.setDesigner(user.getDesignerProfile());
        design.setImageUrlBack(designRequest.getBackImageUrl());
        design.setImageUrlFront(designRequest.getFrontImageUrl());
        design.setName(designRequest.getName());
        design.setTag(String.join(",", designRequest.getTags()));
        design.setSlug(Utils.generateSlug(designRequest.getName()));

        return design;
    }
}
