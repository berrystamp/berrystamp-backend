package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.dtos.request.RequestCustomizationDTo;
import com.berryinkstamp.berrybackendservice.enums.OrderType;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.CustomDesignRequest;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.repositories.CustomDesignRequestRepository;
import com.berryinkstamp.berrybackendservice.repositories.DesignRepository;
import com.berryinkstamp.berrybackendservice.repositories.OrderRequestRepository;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.services.CustomDesignService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomDesignServiceImpl implements CustomDesignService {
    private TokenProvider  tokenProvider;
    private DesignRepository designRepository;
    private ProfileRepository profileRepository;
    private OrderRequestRepository orderRequestRepository;
    private CustomDesignRequestRepository customDesignRequestRepository;


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

    private CustomDesignRequest mapCustomDesignDtoToEntity(CustomDesignRequestDto customDesignRequest){
        var customDesign = new CustomDesignRequest();
        Profile designerProfile = Optional.ofNullable(customDesignRequest.getDesignerProfileId())
                .map(id -> profileRepository.findById(id).orElseThrow(() -> new NotFoundException("No profile with id found")))
                .filter(profile -> profile.getProfileType() == ProfileType.DESIGNER)
                .orElseThrow(() -> new NotFoundException("Profile type customer not found for id"));
        customDesign.setDesignerProfileId(designerProfile.getId());
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
        customDesign.setDesignerProfileId(designerId);
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
