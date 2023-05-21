
package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.ChatMessage;
import com.berryinkstamp.berrybackendservice.models.Conversation;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.repositories.ChatMessageRepository;
import com.berryinkstamp.berrybackendservice.repositories.ConversationRepository;
import com.berryinkstamp.berrybackendservice.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final TokenProvider tokenProvider;

    @Override
    public List<Conversation> fetchAllConversations(ProfileType profileType) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);
        if (profile == null) {
            throw new ForbiddenException("No profile found with profile type");
        }

        List<Conversation> conversations = conversationRepository.findByParticipantsInOrderByLastMessageTimestampDesc(List.of(profile));
        for (Conversation conversation : conversations) {
            List<ChatMessage> messages =  chatMessageRepository.findByConversation(conversation);
            long unreadMessages = messages.stream().filter(message -> message.getReceiver() == profile).filter(m -> !m.isRead()).count();
            conversation.setUnreadMessageCount((int) unreadMessages);
        }

        return conversations;
    }

    @Override
    public Conversation fetchConversationById(ProfileType profileType, Long conversationId) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);
        if (profile == null) {
            throw new ForbiddenException("No profile found with profile type");
        }

        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isEmpty())  {
            throw new NotFoundException("Conversation not found");
        }

        if (conversation.get().getParticipants().contains(profile)) {
            List<ChatMessage> messages =  chatMessageRepository.findByConversation(conversation.get());
            long unreadMessages = messages.stream().filter(message -> message.getReceiver() == profile).filter(m -> !m.isRead()).count();
            conversation.get().setUnreadMessageCount((int) unreadMessages);
            return conversation.get();
        }

        throw new ForbiddenException("You are not authorized to view this resource");
    }


}
