
package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.MockImagesDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateDesignRequest;
import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.DesignLike;
import com.berryinkstamp.berrybackendservice.models.MockImages;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.DesignLikeRepository;
import com.berryinkstamp.berrybackendservice.repositories.DesignRepository;
import com.berryinkstamp.berrybackendservice.repositories.MockImageRepository;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import com.berryinkstamp.berrybackendservice.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DesignServiceImpl implements DesignService {
    private final DesignRepository designRepository;
    private final ProfileRepository profileRepository;
    private final DesignLikeRepository designLikeRepository;
    private final MockImageRepository mockImageRepository;
    private final TokenProvider tokenProvider;

    @PersistenceContext
    private EntityManager entityManager;

    public DesignServiceImpl(DesignRepository designRepository,
                             ProfileRepository profileRepository, DesignLikeRepository designLikeRepository, MockImageRepository mockImageRepository,
                             TokenProvider tokenProvider) {
        this.designRepository = designRepository;
        this.profileRepository = profileRepository;
        this.designLikeRepository = designLikeRepository;
        this.mockImageRepository = mockImageRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    @Override
    public Design createDesign(NewDesignRequest designRequest) {

        if (designNameExistForUser(designRequest.getName(), tokenProvider.getCurrentUser().getDesignerProfile().getId())) {
            throw new BadRequestException("Name already exist for this design!");
        }

        if (designRequest.getAmount().doubleValue() < 0) {
            throw new BadRequestException("Invalid amount");
        }

        Design design = new Design();
        mapDesignRequestDtoToEntity(design, designRequest, tokenProvider.getCurrentUser());
        design = designRepository.save(design);
        Set<MockImages> mocks = createMocks(design, designRequest.getMocks());
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

        if (designRequest.getAmount().doubleValue() < 0) {
            throw new BadRequestException("Invalid amount");
        }

        Design design = mapDesignRequestDtoToEntity(optionalDesign.get(), designRequest, tokenProvider.getCurrentUser());
        design = designRepository.save(design);
        return design;
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
        mockImage.setLimitedStatus(dto.isLimitedStatus());
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
    public Design publicFetchDesignById(Long designId) {
        var design = designRepository.findByIdAndDeletedFalseAndApprovedIsTrue(designId)
                .orElseThrow(()-> new NotFoundException("No design with such Id found!"));
        //TODO: push To Audit
        return design;
    }

    @Override
    public Design fetchDesignById(Long designId, ProfileType profileType) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);

        Design design = designRepository.findByIdAndDeletedFalseAndApprovedIsTrue(designId)
                .orElseThrow(()-> new NotFoundException("No design with such Id found!"));

        Optional<DesignLike> optionalDesignLike = designLikeRepository.findFirstByDesignAndProfile(design, profile);
        if (optionalDesignLike.isPresent()) {
            design.setDesignIsLiked(true);
        }
        //TODO: push To Audit
        return design;
    }

    @Override
    public Design designerGetDesignById(Long designId) {
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
    public Design publicFetchDesignBySlug(String slug) {
        var design = designRepository.findDesignBySlugAndDeletedFalseAndApprovedIsTrue(slug)
                .orElseThrow(() -> new NotFoundException("Design not found!"));

        //TODO: push To audit

        return design;
    }

    @Override
    public Page<Design> publicFetchAllDesign(Long designerId, String tags, String designCategories, String mocks, Integer upperPriceRange, Integer lowerPriceRange, String searchField, Pageable pageable) {
        if(designerId == null && tags == null && designCategories == null && mocks == null && upperPriceRange == null && lowerPriceRange == null){
            return designRepository.findByDeletedAndApprovedIsTrue(false, pageable);
        }

        List<Design> res = filterDesigns(designerId, tags, designCategories, mocks, upperPriceRange, lowerPriceRange, searchField);

        return new PageImpl<>(res, pageable, res.size());

    }

    @Override
    public Page<Design> fetchAllDesign(Long designerId, String tags, String designCategories, String mocks, Integer upperPriceRange, Integer lowerPriceRange, String searchField, ProfileType profileType, Pageable pageable) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);

        if(designerId == null && tags == null && designCategories == null && mocks == null && upperPriceRange == null && lowerPriceRange == null){
            return designRepository.findByDeletedAndApprovedIsTrue(false, pageable);
        }

        List<Design> res = filterDesigns(designerId, tags, designCategories, mocks, upperPriceRange, lowerPriceRange, searchField);

        res.forEach(design -> {
            Optional<DesignLike> optionalDesignLike = designLikeRepository.findFirstByDesignAndProfile(design, profile);
            if (optionalDesignLike.isPresent()) {
                design.setDesignIsLiked(true);
            }
        });


        return new PageImpl<>(res, pageable, res.size());
    }

    @Override
    public Page<Design> fetchAllLikedDesign(ProfileType profileType, Pageable pageable) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);

        List<Design> res = designLikeRepository.findByProfile(profile).stream().map(DesignLike::getDesign).collect(Collectors.toList());

        res.forEach(design -> {
            design.setDesignIsLiked(true);
        });

        return new PageImpl<>(res, pageable, res.size());

    }

    @Override
    public Design acceptDesign(Long designId, boolean approved) {
        Admin admin = tokenProvider.getCurrentAdmin();
        if (!admin.isSuperAdmin() && !admin.getPermission().isCanManagerDesigns()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }

       Optional<Design> optionalDesign = designRepository.findById(designId);
       if (optionalDesign.isEmpty()){
           throw new NotFoundException("Design not found with this ID: " + designId);
       }

       Design design = optionalDesign.get();
       design.setStatus(approved ? DesignStatus.APPROVED : DesignStatus.DECLINED);
       design.setApproved(approved);
       design = designRepository.save(design);

       //todo push to audit
        //todo email and push notification
       return design;

    }

    @Override
    public Page<Design> adminFetchAllDesign(DesignStatus designStatus, Pageable pageable) {
        if (designStatus == null) {
            return designRepository.findByDeletedFalse(pageable);
        }
        return designRepository.findByDeletedFalseAndStatus(designStatus, pageable);
    }

    @Override
    public Design likeAndUnlikeDesign(Long designId, ProfileType profileType) {
        boolean designIsLiked = true;
        Optional<Design> optionalDesign = designRepository.findByIdAndDeletedFalseAndApprovedIsTrue(designId);
        if (optionalDesign.isEmpty()){
            throw new NotFoundException("Design not found with this ID: " + designId);
        }

        Design design = optionalDesign.get();
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);
        Optional<DesignLike> optionalDesignLike = designLikeRepository.findFirstByDesignAndProfile(design, profile);
        if (optionalDesignLike.isPresent()) {
            designIsLiked = false;
            designLikeRepository.delete(optionalDesignLike.get());
        } else {
            designLikeRepository.save(new DesignLike(design, profile));
        }

        design.setDesignIsLiked(designIsLiked);
        return design;
    }

    private Set<MockImages> createMocks(Design design, List<MockImagesDto> mockImagesDtos) {
        HashSet<MockImages> mockImages = new HashSet<>();
        mockImagesDtos.forEach(a -> {
            MockImages mockImage = new MockImages();
            mockImage.setName(a.getName());
            mockImage.setAvailableQty(a.getAvailableQty());
            mockImage.setImageUrl(a.getImageUrl());
            mockImage.setLimitedStatus(a.isLimitedStatus());
            mockImage.setDesign(design);
            mockImages.add(mockImageRepository.save(mockImage));
        });
        return mockImages;
    }

    private List<Predicate> getPredicates(Profile designer, String tags, String designCategories,String mocks, Integer upperPriceRange, Integer lowerPriceRange,String searchField, CriteriaBuilder qb, Root<Design> root, CriteriaQuery<Design> cq) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(qb.isFalse(root.get("deleted")));

        predicates.add(qb.isTrue(root.get("approved")));

        if (Objects.nonNull(designer)) {
            predicates.add(qb.equal(root.get("designer"), designer));
        }

        if (Objects.nonNull(upperPriceRange)) {
            predicates.add(qb.lessThanOrEqualTo(root.get("amount"), upperPriceRange));
        }

        if (Objects.nonNull(lowerPriceRange)) {
            predicates.add(qb.greaterThanOrEqualTo(root.get("amount"), lowerPriceRange));
        }

        if (Objects.nonNull(tags)) {
            Arrays.stream(tags.split(",")).forEach(tag -> {
                qb.or(qb.like(root.get("tag"), "%" + tag + "%"));
            });
        }

        if (Objects.nonNull(designCategories)) {
            Arrays.stream(designCategories.split(",")).forEach(category -> {
                qb.or(qb.like(root.get("category"), "%" + category + "%"));
            });
        }


        if (Objects.nonNull(mocks)) {
            Arrays.stream(mocks.split(",")).forEach(mock -> {
                Subquery<Long> subquery1 = cq.subquery(Long.class);
                Root<Design> subqueryDesign1 = subquery1.from(Design.class);
                Join<MockImages, Design> subqueryMocks = subqueryDesign1.join("mocks");
                subquery1.select(subqueryDesign1.get("id")).where(qb.like(subqueryMocks.get("name"), "%" + mock + "%"));
                qb.or(qb.in(root.get("id")).value(subquery1));
            });
        }

        if (Objects.nonNull(searchField)) {
            qb.or(qb.like(root.get("tag"), "%" + searchField + "%"));
            qb.or(qb.like(root.get("category"), "%" + searchField + "%"));

            Subquery<Long> subquery1 = cq.subquery(Long.class);
            Root<Design> subqueryDesign1 = subquery1.from(Design.class);
            Join<MockImages, Design> subqueryMocks = subqueryDesign1.join("mocks");
            subquery1.select(subqueryDesign1.get("id")).where(qb.like(subqueryMocks.get("name"), "%" + searchField + "%"));
            qb.or(qb.in(root.get("id")).value(subquery1));

            Subquery<Long> subquery2 = cq.subquery(Long.class);
            Root<Design> subqueryDesign2 = subquery2.from(Design.class);
            Join<Profile, Design> subqueryDesigner = subqueryDesign2.join("designer");
            subquery2.select(subqueryDesign2.get("id")).where(qb.like(subqueryDesigner.get("name"), "%" + searchField + "%"));
            qb.or(qb.in(root.get("id")).value(subquery2));
        }

        return predicates;


    }

    private Boolean designNameExistForUser(String designName, Long designerProfileId){
        Optional<Design>design = designRepository.findDesignByDesignerIdAndNameAndDeletedFalse(designerProfileId, designName);
        return design.isPresent();
    }

    private Boolean designNameExistForUser(Design design, Profile designer, String name){
        return designRepository.existsByNameAndDesignerAndIdNot(name, designer, design.getId());
    }

    private void mapDesignRequestDtoToEntity(Design design, NewDesignRequest designRequest, User user){
        design.setAmount(designRequest.getAmount());
        design.setDescription(designRequest.getDescription());
        if (designRequest.getCategory() != null ) {
            design.setCategory(String.join(",", designRequest.getCategory()));
        }

        if (designRequest.getTags() != null ) {
            design.setTag(String.join(",", designRequest.getTags()));
        }
        design.setPrinter(profileRepository.findById(designRequest.getPrinterId()).get());
        design.setDesigner(user.getDesignerProfile());
        design.setImageUrlBack(designRequest.getBackImageUrl());
        design.setImageUrlFront(designRequest.getFrontImageUrl());
        design.setName(designRequest.getName());
        design.setSlug(Utils.generateSlug(designRequest.getName()));
        design.setStatus(DesignStatus.AWAITING_APPROVAL);
        design.setApproved(false);

    }

    private Design mapDesignRequestDtoToEntity(Design design, UpdateDesignRequest designRequest, User user){
        design.setAmount(designRequest.getAmount());
        design.setDescription(designRequest.getDescription());
        String categories = designRequest.getCategory() == null ? null : String.join(",", designRequest.getCategory());
        design.setCategory(categories);


        String tags = designRequest.getTags() == null ? null : String.join(",", designRequest.getTags());
        design.setTag(tags);

        design.setPrinter(profileRepository.findById(designRequest.getPrinterId()).get());
        design.setDesigner(user.getDesignerProfile());
        design.setImageUrlBack(designRequest.getBackImageUrl());
        design.setImageUrlFront(designRequest.getFrontImageUrl());
        design.setName(designRequest.getName());
        return design;
    }

    private List<Design> filterDesigns(Long designerId, String tags, String designCategories, String mocks, Integer upperPriceRange, Integer lowerPriceRange, String searchField) {
        Profile designer = null;

        if (designerId != null) {
            designer = profileRepository.findById(designerId).orElse(null);
        }
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Design> cq = qb.createQuery(Design.class);

        Root<Design> root = cq.from(Design.class);

        List<Predicate> predicates = getPredicates(designer, tags, designCategories, mocks, upperPriceRange, lowerPriceRange, searchField, qb, root, cq);

        cq.select(root).where(predicates.toArray(new Predicate[]{}));

        List<Design> res = entityManager.createQuery(cq).getResultList();
        return res;
    }


}
