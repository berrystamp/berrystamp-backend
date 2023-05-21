package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Conversation;

import java.util.List;

public interface ConversationService {
    List<Conversation> fetchAllConversations(ProfileType profileType);

    Conversation fetchConversationById(ProfileType profileType, Long conversationId);
}
