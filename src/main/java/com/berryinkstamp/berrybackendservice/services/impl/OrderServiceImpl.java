package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderDto;
import com.berryinkstamp.berrybackendservice.dtos.request.PrintRequestDto;
import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.enums.OrderType;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.exceptions.UnknownException;
import com.berryinkstamp.berrybackendservice.models.*;
import com.berryinkstamp.berrybackendservice.repositories.*;
import com.berryinkstamp.berrybackendservice.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private ProfileRepository profileRepository;
    private PrintRequestRepository printRequestRepository;
    private DesignRepository designRepository;
    private TokenProvider tokenProvider;
    private MockImageRepository mockImageRepository;
    private OrderRequestRepository orderRequestRepository;
    @Override
    public Order createNewOrder(OrderDto orderDto) {
        var orderRequest = orderRequestRepository.findById(orderDto.getOrderRequestId())
                .orElseThrow(() -> new NotFoundException("No such order request Id found"));
        return Optional.ofNullable(tokenProvider.getCurrentUser())
             .map(user -> mapOrderCreationToEntity(orderDto,user, orderRequest)).orElseThrow(() -> new UnknownException("an error occurred"));
    }

    private Order mapOrderCreationToEntity(OrderDto orderDto, User user, OrderRequest orderRequest){
        var order = new Order();
        order.setTitle(orderDto.getOrderTitle());
        order.setDescription(orderDto.getDescription());
        order.setOrderRequest(orderRequest);
        order.setPickupAmount(orderDto.getPickUpAmount());
        Profile profile = orderRequest.getOrderType() == OrderType.PRINT? user.getProfile(ProfileType.PRINTER): user.getProfile(ProfileType.DESIGNER);
       order.setDesignerOrPrinterProfile(profile);
        //TODO: push to audit
      return orderRepository.save(order);
    }

    @Override
    public Page<Order> fetchAllOrders(OrderStatus orderStatus, Pageable pageable) {
        return orderRepository.findAllByOrderStatus(orderStatus, pageable);
    }

    @Override
    public void orderDecision(Long orderId, Boolean decision) {
        var profile = tokenProvider.getCurrentUser().getProfile(ProfileType.CUSTOMER);
        orderRepository.findOrderByCustomerProfileAndId(profile, orderId)
                .map(order -> {
                    if (decision) {
                        order.setOrderStatus(OrderStatus.ACTIVE);
                    } else {
                        order.setOrderStatus(OrderStatus.REJECTED);
                    }
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new NotFoundException("Order with id for profile not found"));
    }

    @Override
    public Page<Order> fetchAllLoggedInCustomerOrders(ProfileType profileType, Pageable pageable) {
        return Optional.ofNullable(tokenProvider.getCurrentUser().getProfile(profileType))
                .map(profile -> orderRepository.findAllByCustomerProfile(profile,pageable))
                .orElseThrow(() -> new UnknownException("an error occurred"));
    }

    @Override
    public OrderRequest createNewOrderRequest(PrintRequestDto printRequestDto) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(ProfileType.CUSTOMER);
        return designRepository.findById(printRequestDto.getDesignId())
                .map(design -> mapToPrintRequestEntity(design, printRequestDto))
                .map(printRequest -> mapPrintRequestToOrderRequestEntity(printRequest,printRequestDto,profile))
                .orElseThrow(() -> new NotFoundException("No design with Id present"));
    }

    @Override
    public Order fetchOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with Id does not exist"));
    }

    private OrderRequest mapPrintRequestToOrderRequestEntity(PrintRequest printRequest, PrintRequestDto printRequestDto, Profile customer){
        var orderRequest = new OrderRequest();
        orderRequest.setBudgetAmount(printRequestDto.getEstimatedAmount());
        orderRequest.setPrintRequest(printRequest);
        orderRequest.setOrderType(OrderType.PRINT);
        orderRequest.setDateOfDelivery(printRequestDto.getDateOfDelivery());
        orderRequest.setCustomerProfile(customer);
      return orderRequestRepository.save(orderRequest);
    }

    private PrintRequest mapToPrintRequestEntity(Design design, PrintRequestDto printRequestDto){
        MockImages mockImages = mockImageRepository.findByIdAndDesign(printRequestDto.getMockItemId(), design).orElseThrow(() -> new NotFoundException("Mock Image not found"));
        Profile printerProfile = profileRepository.findById(printRequestDto.getPrinterId()).orElseThrow(() -> new NotFoundException("Profile with Id not found"));

        if (design.getPrinter() != null  && !Objects.equals(design.getPrinter(), printRequestDto.getPrinterId())) {
            throw new BadRequestException("This printer is not allowed to print this item");
        }

        var printRequest = new PrintRequest();
        printRequest.setColour(printRequestDto.getColour());
        printRequest.setMockItemUrl(mockImages.getImageUrl());
        printRequest.setDesignBackImageUrl(design.getImageUrlBack());
        printRequest.setDesignFrontImageUrl(design.getImageUrlFront());
        printRequest.setPrinterProfile(printerProfile);
        printRequest.setSize(printRequestDto.getSize());
        printRequest.setSourceOfItem(printRequestDto.getSourceOfItem());
        return printRequestRepository.save(printRequest);
    }

}
