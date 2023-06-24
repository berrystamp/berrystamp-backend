package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.dtos.request.MockImagesDto;
import com.berryinkstamp.berrybackendservice.dtos.request.NewDesignRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RequestCustomizationDTo;
import com.berryinkstamp.berrybackendservice.enums.OrderType;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.*;
import com.berryinkstamp.berrybackendservice.repositories.*;
import com.berryinkstamp.berrybackendservice.services.CustomDesignService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomDesignServiceImpl implements CustomDesignService {
    private final TokenProvider  tokenProvider;
    private final DesignRepository designRepository;
    private final ProfileRepository profileRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final CustomDesignRequestRepository customDesignRequestRepository;
    private final MockImageRepository mockImageRepository;
    private final CustomDesignRepository customDesignRepository;


    @Override
    public OrderRequest createCustomDesignRequest(CustomDesignRequestDto customDesignRequestDto) {
        Profile customerProfile = tokenProvider.getCurrentUser().getCustomerProfile();
        return Optional.ofNullable(customDesignRequestDto)
                .map(profile -> mapCustomDesignDtoToEntity(customDesignRequestDto))
                .map(customDesignRequest -> mapToOrderRequest(customDesignRequest,customDesignRequestDto, customerProfile))
                .orElse(new OrderRequest());
    }

    @Override
    public OrderRequest createCustomizedDesign(RequestCustomizationDTo dto) {
        Profile customerProfile = tokenProvider.getCurrentUser().getCustomerProfile();
        return designRepository.findById(dto.getDesignId())
                .map(design -> mapFromDesignToCustomDesign(design, dto.getDesignId()))
                .map(customDesignRequest -> mapFromCustomDesignRequestToOrderRequest(customDesignRequest, customerProfile))
                .orElseThrow(()-> new NotFoundException("No such design with Id exist"));
    }

    @Override
    public CustomDesign uploadCustomDesign(NewDesignRequest designRequest,  Long orderId) {
        Profile designerProfile = tokenProvider.getCurrentUser().getDesignerProfile();
        return Optional.ofNullable(designerProfile)
                .map(profile ->mapNewDesignDtoToEntity(profile, designRequest, orderId))
                .orElseThrow(() -> new NotFoundException("Profile designer does not exist for this user"));
    }

    @Override
    public CustomDesign fetchCustomDesignByOrderId(Long orderId) {
        return customDesignRepository.findCustomDesignByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("No custom design found for such orderId"));
    }

    @Override
    public List<CustomDesign> fetchAllCompletedCustomDesign() {
        Profile profile = tokenProvider.getCurrentUser().getProfile(ProfileType.CUSTOMER);
        return customDesignRepository.findCustomDesignByCustomerProfileAndIsCompletedIsTrue(profile);
    }

    private CustomDesign mapNewDesignDtoToEntity(Profile profile, NewDesignRequest designRequest, Long orderId){
        var customDesign1 = customDesignRepository.findCustomDesignByOrderId(orderId);
        if(customDesign1.isEmpty()) {
            var customDesign = new CustomDesign();
            Set<MockImages> mockImages = designRequest.getMocks()
                    .stream()
                    .map(mockImagesDto -> mapMockImagesDtoToEntity(mockImagesDto))
                    .collect(Collectors.toSet());
            customDesign.setMocks(mockImages);
            customDesign.setOrderId(orderId);
            customDesign.setDesignerProfile(profile);
            customDesign.setImageUrlFront(designRequest.getFrontImageUrl());
            customDesign.setImageUrlBack(designRequest.getBackImageUrl());

            return customDesignRepository.save(customDesign);
        }else{
            Set<MockImages> mockImagesSet = customDesign1.get().getMocks();
            mockImageRepository.deleteAll(mockImagesSet);
            Set<MockImages> mockImages = designRequest.getMocks()
                    .stream()
                    .map(mockImagesDto -> mapMockImagesDtoToEntity(mockImagesDto))
                    .collect(Collectors.toSet());
            customDesign1.get().setMocks(mockImages);
            customDesign1.get().setOrderId(orderId);
            customDesign1.get().setDesignerProfile(profile);
            customDesign1.get().setImageUrlFront(designRequest.getFrontImageUrl());
            customDesign1.get().setImageUrlBack(designRequest.getBackImageUrl());
            return customDesignRepository.save(customDesign1.get());
        }
    }

    private MockImages mapMockImagesDtoToEntity(MockImagesDto mockImagesDto){
        var mockImage = new MockImages();
        mockImage.setAvailableQty(mockImagesDto.getAvailableQty());
        mockImage.setImageUrl(mockImagesDto.getImageUrl());
        mockImage.setName(mockImage.getName());
        return mockImageRepository.save(mockImage);
    }

    private CustomDesignRequest mapCustomDesignDtoToEntity(CustomDesignRequestDto customDesignRequest){
        var customDesign = new CustomDesignRequest();
        Profile designerProfile = Optional.ofNullable(customDesignRequest.getDesignerProfileId())
                .map(id -> profileRepository.findById(id).orElseThrow(() -> new NotFoundException("No profile with id found")))
                .filter(profile -> profile.getProfileType() == ProfileType.DESIGNER)
                .orElseThrow(() -> new NotFoundException("Profile type customer not found for id"));
        customDesign.setPurpose(customDesignRequest.getPurpose());
        customDesign.setMockTypes(String.join(",", customDesignRequest.getMockTypes()));
        customDesign.setTheme(customDesignRequest.getTheme());
        return customDesignRequestRepository.save(customDesign);
    }

    private OrderRequest mapToOrderRequest(CustomDesignRequest customDesign, CustomDesignRequestDto customDesignRequestDto, Profile customerProfile){
        var orderRequest = new OrderRequest();
        orderRequest.setOrderType(OrderType.DESIGN);
        orderRequest.setCustomDesignRequest(customDesign);
        orderRequest.setDateOfDelivery(customDesignRequestDto.getDateOfDelivery());
        orderRequest.setCustomerProfile(customerProfile);

        return orderRequestRepository.save(orderRequest);
    }

    private CustomDesignRequest mapFromDesignToCustomDesign(Design design, Long designerId){
        Profile deignerProfile = profileRepository.findById(designerId).orElseThrow(() -> new NotFoundException("No profile with id found"));
        var customDesign = new CustomDesignRequest();
        customDesign.setIsReferenceDesign(true);
        customDesign.setImageUrlFront(design.getImageUrlFront());
        customDesign.setImageUrlBack(design.getImageUrlBack());
        customDesign.setDesignId(design.getId());

        return customDesignRequestRepository.save(customDesign);
    }

    private OrderRequest mapFromCustomDesignRequestToOrderRequest(CustomDesignRequest customDesign, Profile customerProfile){
        var orderRequest = new OrderRequest();
        orderRequest.setCustomDesignRequest(customDesign);
        orderRequest.setCustomerProfile(customerProfile);
        orderRequest.setOrderType(OrderType.DESIGN);

        return orderRequestRepository.save(orderRequest);

    }
}
