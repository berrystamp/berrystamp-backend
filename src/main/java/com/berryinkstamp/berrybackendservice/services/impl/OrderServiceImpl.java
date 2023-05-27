package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderDto;
import com.berryinkstamp.berrybackendservice.dtos.request.PrintRequestDto;
import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.enums.OrderType;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.*;
import com.berryinkstamp.berrybackendservice.repositories.*;
import com.berryinkstamp.berrybackendservice.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private ProfileRepository profileRepository;
    private PrintRequestRepository printRequestRepository;
    private DesignRepository designRepository;
    private TokenProvider tokenProvider;
    private CustomDesignRepository customDesignRepository;
    private OrderRequestRepository orderRequestRepository;
    @Override
    public Order createNewOrder(OrderDto orderDto) {
        var orderRequest = orderRequestRepository.findById(orderDto.getOrderRequestId())
                .orElseThrow(() -> new NotFoundException("No such order request Id found"));
        return Optional.ofNullable(tokenProvider.getCurrentUser())
             .map(user -> mapOrderCreationToEntity(orderDto,user, orderRequest))
             .get();
    }

    private Order mapOrderCreationToEntity(OrderDto orderDto, User user, OrderRequest orderRequest){
        var order = new Order();
        order.setTitle(orderDto.getOrderTitle());
        order.setDescription(orderDto.getDescription());
        order.setOrderRequest(orderRequest);
//        order.setDesignAmount(orderCreationRequest.getDesignAmount());
//        order.setPrintingAmount(orderRequest.getPrintAmount());
//        order.setDateOfDelivery(orderCreationRequest.getDateOfDelivery());
        order.setPickupAmount(orderDto.getPickUpAmount());
        Profile profile = orderRequest.getOrderType() == OrderType.PRINT? user.getProfile(ProfileType.PRINTER): user.getProfile(ProfileType.DESIGNER);
       order.setDesignerOrPrinterProfile(profile);
        //TODO: push to audit
      return orderRepository.save(order);
    }

    @Override
    public Page<Order> fetchAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public void orderDecision(Long orderId, Boolean decision) {
        orderRepository.findById(orderId)
                .map(order -> {
                    if (decision) {
                        order.setOrderStatus(OrderStatus.ACTIVE);
                    } else {
                        order.setOrderStatus(OrderStatus.REJECTED);
                    }
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new NotFoundException("Order with id not found"));
    }

    @Override
    public Page<Order> fetchAllLoggedInCustomerOrders(Pageable pageable) {
        return Optional.ofNullable(tokenProvider.getCurrentUser())
                .map(User::getCustomerProfile)
                .map(profile -> orderRepository.findAllByCustomerProfile(profile,pageable))
                .get();
    }

    @Override
    public OrderRequest createNewOrderRequest(PrintRequestDto printRequestDto, Long designId) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(ProfileType.CUSTOMER);
        return designRepository.findById(designId)
                .map(design -> mapToPrintRequestEntity(design, printRequestDto))
                .map(printRequest -> mapPrintRequestToOrderRequestEntity(printRequest,printRequestDto,profile))
                .orElseThrow(() -> new NotFoundException("No design with Id present"));

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
        var printRequest = new PrintRequest();
        Profile printerProfile = profileRepository.findById(printRequestDto.getPrinterId())
                .orElseThrow(() -> new NotFoundException("Profile with Id not found"));
        printRequest.setColour(printRequestDto.getColour());
        printRequest.setMockItemUrl(printRequestDto.getMockItemUrl());
        printRequest.setDesignBackImageUrl(printRequestDto.getDesignBackImageUrl());
        printRequest.setDesignFrontImageUrl(printRequestDto.getDesignFrontImageUrl());
        printRequest.setPrinterProfile(printerProfile);
        printRequest.setSize(printRequestDto.getSize());
        printRequest.setSourceOfItem(printRequestDto.getSourceOfItem());
//        printRequest.setDateOfDelivery(printRequestDto.getDateOfDelivery());

        return printRequestRepository.save(printRequest);
    }

}
