package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.MarkMessageAsRead;
import com.berryinkstamp.berrybackendservice.dtos.request.Message;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.ChatMessage;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MessageService {
    List<ChatMessage> fetchAllMessages(ProfileType profileType, Long conversationId);

    void processIndividualMessage(Message msg, Authentication principal);

    void markMessageAsRead(MarkMessageAsRead markMessageAsRead, Authentication principal);

    void processOrderMessage(Message msg, Authentication principal);

}
