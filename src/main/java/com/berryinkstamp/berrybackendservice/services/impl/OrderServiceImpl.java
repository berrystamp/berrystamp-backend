package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderDto;
import com.berryinkstamp.berrybackendservice.dtos.request.OrderPaymentDto;
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
import com.berryinkstamp.berrybackendservice.services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProfileRepository profileRepository;
    private final PrintRequestRepository printRequestRepository;
    private final DesignRepository designRepository;
    private final TokenProvider tokenProvider;
    private final MockImageRepository mockImageRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final PaymentService paymentService;
    @Override
    public Order createNewOrder(OrderDto orderDto) {
        //todo validate amount is not negative richard
        if(orderDto.getPickUpAmount().compareTo(BigDecimal.ZERO) < 1){
            throw new BadRequestException("Non positive value not allowed for pickup amount");
        }
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
        //TODO: push to audit
      return orderRepository.save(order);
    }

    @Override
    public Page<Order> fetchAllOrders(OrderStatus orderStatus, Pageable pageable) {
        if (orderStatus == null) {
            return orderRepository.findAll(pageable);
        }
        return orderRepository.findAllByOrderStatus(orderStatus, pageable);
    }

    @Override
    public Map<String, String> payForOrder(OrderPaymentDto orderPaymentDto) {
        Order order = orderRepository.findById(orderPaymentDto.getOrderId()).orElseThrow(()-> new NotFoundException("Order not found"));
        if(order.getPaid()){
            throw new BadRequestException("Order has been paid already!");
        }
        if(order.getPaymentUrl()!=null){
          return Map.of("paymentUrl", order.getPaymentUrl());
        }
        String ref = UUID.randomUUID().toString();
        String authUrl = paymentService.initializeTransaction(order.getTotalAmount(), orderPaymentDto.getCallback(), ref);
        order.setPaymentUrl(authUrl);
        order.setTransactionRef(ref);
        orderRepository.save(order);

        return Map.of("paymentUrl", authUrl);
    }

    @Override
    public void orderDecision(Long orderId, Boolean decision) {
        //todo ensure order has been paid for.
        //todo implement transactions with paystack
        var profile = tokenProvider.getCurrentUser().getProfile(ProfileType.CUSTOMER);
        orderRepository.findOrderByOrderRequest_CustomerProfileAndId(profile, orderId)
                .map(order -> {
                    order.setOrderStatus(OrderStatus.REJECTED);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new NotFoundException("Order with id for profile not found"));
    }

    @Override
    public Page<Order> fetchAllLoggedInCustomerOrders(ProfileType profileType, Pageable pageable) {
        return Optional.ofNullable(tokenProvider.getCurrentUser().getProfile(profileType))
                .map(profile -> orderRepository.findAllByOrderRequest_CustomerProfile(profile,pageable))
                .orElseThrow(() -> new UnknownException("an error occurred"));
    }

    @Override
    @Transactional
    public OrderRequest createNewOrderRequest(PrintRequestDto printRequestDto) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(ProfileType.CUSTOMER);
        //todo validate the printerId is really a printer richard
        //todo validate the designId and mockId richard
        //todo validate that the mock really belongs to the design richard
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
        Profile printerProfile = profileRepository.findById(printRequestDto.getPrinterId()).orElseThrow(() -> new NotFoundException("Profile with Id not found"));
        if(!printerProfile.getProfileType().equals(ProfileType.PRINTER)){
            throw new BadRequestException("Profile type not of type printer!");
        }
        orderRequest.setBudgetAmount(printRequestDto.getEstimatedAmount());
        orderRequest.setPrintRequest(printRequest);
        orderRequest.setOrderType(OrderType.PRINT);
        orderRequest.setDateOfDelivery(printRequestDto.getDateOfDelivery());
        orderRequest.setCustomerProfile(customer);
        orderRequest.setDesignerOrPrinterProfile(printerProfile);
      return orderRequestRepository.save(orderRequest);
    }

    private PrintRequest mapToPrintRequestEntity(Design design, PrintRequestDto printRequestDto){
        MockImages mockImages = mockImageRepository.findByIdAndDesign(printRequestDto.getMockItemId(), design).orElseThrow(() -> new NotFoundException("Mock Image not found"));

        if (design.getPrinter() != null  && !Objects.equals(design.getPrinter(), printRequestDto.getPrinterId())) {
            throw new BadRequestException("This printer is not allowed to print this item");
        }

        var printRequest = new PrintRequest();
        printRequest.setColour(printRequestDto.getColour());
        printRequest.setMockItemUrl(mockImages.getImageUrl());
        printRequest.setDesignBackImageUrl(design.getImageUrlBack());
        printRequest.setDesignFrontImageUrl(design.getImageUrlFront());
        printRequest.setSize(printRequestDto.getSize());
        printRequest.setSourceOfItem(printRequestDto.getSourceOfItem());
        return printRequestRepository.save(printRequest);
    }

}
