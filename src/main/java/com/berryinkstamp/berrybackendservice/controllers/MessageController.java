package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.Message;
import com.berryinkstamp.berrybackendservice.models.ChatMessage;
import com.berryinkstamp.berrybackendservice.services.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
public class MessageController {


    private final MessageService messageService;


    @MessageMapping("/topic/individual/conversation")
    public void processIndividualMessage(@Payload Message msg, Authentication accessor) {
        messageService.processIndividualMessage(msg, accessor);
    }

    @MessageMapping("/topic/order/conversation")
    public void processOrderMessage(@Payload Message msg, Authentication accessor) {
        messageService.processOrderMessage(msg, accessor);
    }

    @MessageMapping("/chat.markAsRead")
    public void markMessageAsRead(@Payload Long messageId) {
       messageService.markMessageAsRead(messageId);
    }
}
