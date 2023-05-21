package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.ChatMessage;
import com.berryinkstamp.berrybackendservice.models.Conversation;
import com.berryinkstamp.berrybackendservice.services.ConversationService;
import com.berryinkstamp.berrybackendservice.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/v1/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    private final MessageService messageService;

    @Operation(summary = "Fetch All Conversations", description =  "Fetch All Conversations")
    @GetMapping
    private List<Conversation> fetchAllConversations(@RequestHeader(value = "profileType") ProfileType profileType){
        return conversationService.fetchAllConversations(profileType);
    }


    @Operation(summary = "Fetch Conversation By Id", description =  "Fetch Conversation By Id")
    @GetMapping("/{conversationId}")
    private Conversation fetchConversationById(@RequestHeader(value = "profileType") ProfileType profileType, @PathVariable Long conversationId){
        return conversationService.fetchConversationById(profileType, conversationId);
    }

    @Operation(summary = "Fetch Conversation Messages", description =  "Fetch Conversation Messages")
    @GetMapping("/{conversationId}/messages")
    private List<ChatMessage> fetchConversationMessages(@RequestHeader(value = "profileType") ProfileType profileType, @PathVariable Long conversationId){
        return messageService.fetchAllMessages(profileType, conversationId);
    }
}
