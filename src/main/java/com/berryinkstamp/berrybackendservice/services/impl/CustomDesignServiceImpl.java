package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.CustomDesignRequestDto;
import com.berryinkstamp.berrybackendservice.enums.OrderType;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.CustomDesignRequest;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.OrderRequest;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.repositories.CustomDesignRepository;
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
    private CustomDesignRepository customDesignRepository;
    private OrderRequestRepository orderRequestRepository;
    @Override
    public OrderRequest uploadCustomDesign(CustomDesignRequestDto customDesignRequestDto) {
        Profile customerProfile = tokenProvider.getCurrentUser().getCustomerProfile();
        return Optional.ofNullable(customDesignRequestDto)
                .map(profile -> mapCustomDesignDtoToEntity(customDesignRequestDto))
                .map(customDesignRequest -> mapToOrderRequest(customDesignRequest,customDesignRequestDto, customerProfile))
                .orElse(new OrderRequest());
    }

    @Override
    public OrderRequest createCustomizedDesign(Long designId) {
        Profile customerProfile = tokenProvider.getCurrentUser().getCustomerProfile();
        return designRepository.findById(designId)
                .map(design -> mapFromDesignToCustomDesign(design))
                .map(customDesignRequest -> mapFromCustomDesignRequestToOrderRequest(customDesignRequest, customerProfile))
                .orElseThrow(()-> new NotFoundException("No such design with Id exist"));
    }

    private CustomDesignRequest mapCustomDesignDtoToEntity(CustomDesignRequestDto customDesignRequest){
        var customDesign = new CustomDesignRequest();
        Profile designerProfile = Optional.ofNullable(customDesignRequest.getDesignerProfileId())
                .map(id -> profileRepository.findById(id).orElseThrow(() -> new NotFoundException("No profile with id found")))
                .filter(profile -> profile.getProfileType() == ProfileType.DESIGNER)
                .orElseThrow(() -> new NotFoundException("Profile type customer not found for id"));
        customDesign.setDesignerProfile(designerProfile);
        customDesign.setImageUrlBack(customDesignRequest.getImageUrlBack());
        customDesign.setImageUrlFront(customDesignRequest.getImageUrlFront());
        customDesign.setPurpose(customDesignRequest.getPurpose());
        customDesign.setMockTypes(customDesignRequest.getMockTypes());
        customDesign.setTheme(customDesignRequest.getTheme());

        return customDesignRepository.save(customDesign);
    }

    private OrderRequest mapToOrderRequest(CustomDesignRequest customDesignRequest, CustomDesignRequestDto customDesignRequestDto, Profile customerProfile){
        var orderRequest = new OrderRequest();
        orderRequest.setOrderType(OrderType.DESIGN);
        orderRequest.setCustomDesignRequest(customDesignRequest);
        orderRequest.setDateOfDelivery(customDesignRequestDto.getDateOfDelivery());
        orderRequest.setCustomerProfile(customerProfile);

        return orderRequestRepository.save(orderRequest);
    }

    private CustomDesignRequest mapFromDesignToCustomDesign(Design design){
        var customDesign = new CustomDesignRequest();
        customDesign.setIsReferenceDesign(true);
        customDesign.setDesignerProfile(design.getDesigner());

        return customDesignRepository.save(customDesign);
    }

    private OrderRequest mapFromCustomDesignRequestToOrderRequest(CustomDesignRequest customDesignRequest, Profile customerProfile){
        var orderRequest = new OrderRequest();
        orderRequest.setCustomDesignRequest(customDesignRequest);
        orderRequest.setCustomerProfile(customerProfile);
        orderRequest.setOrderType(OrderType.DESIGN);

        return orderRequestRepository.save(orderRequest);

    }
}
