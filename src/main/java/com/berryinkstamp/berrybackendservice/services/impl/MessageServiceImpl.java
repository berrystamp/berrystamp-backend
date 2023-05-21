
package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.Message;
import com.berryinkstamp.berrybackendservice.enums.ConversationType;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.ChatMessage;
import com.berryinkstamp.berrybackendservice.models.Conversation;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.ChatMessageRepository;
import com.berryinkstamp.berrybackendservice.repositories.ConversationRepository;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.repositories.UserRepository;
import com.berryinkstamp.berrybackendservice.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final ConversationRepository conversationRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final TokenProvider tokenProvider;

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;



    @Override
    public List<ChatMessage> fetchAllMessages(ProfileType profileType, Long conversationId) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);
        if (profile == null) {
            throw new ForbiddenException("No profile found with profile type");
        }


        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isEmpty())  {
            throw new NotFoundException("Conversation not found");
        }

        if (conversation.get().getParticipants().contains(profile)) {
            return chatMessageRepository.findByConversation(conversation.get());
        }

        throw new ForbiddenException("You are not authorized to view this resource");
    }


    @Override
    public void processIndividualMessage(Message msg, Authentication principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (user.isEmpty()) {
            return;
        }

        Optional<Profile> from = profileRepository.findByIdAndUser(msg.getFromProfileId(), user.get());
        if (from.isEmpty()) {
            return;
        }

        Optional<Profile> to = profileRepository.findById(msg.getToProfileId());
        if (to.isEmpty()) {
            return;
        }

        Conversation conversation = getOrCreateConversation(from.get(), to.get());

        ChatMessage message = new ChatMessage();
        message.setMessageIdentifier(UUID.randomUUID().toString());
        message.setContent(msg.getContent());
        message.setConversation(conversation);
        message.setOrderRequest(message.isOrderRequest());
        message.setRead(false);
        message.setReceiver(to.get());
        message.setSender(from.get());
        message = chatMessageRepository.save(message);

        messagingTemplate.convertAndSendToUser(conversation.getConversationName(), "/topic/individual/conversation", message);

    }

    @Override
    public void markMessageAsRead(Long messageId) {
        Optional<ChatMessage> optionalMessage = chatMessageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) {
            return;
        }

        ChatMessage message = optionalMessage.get();
        message.setRead(true);
        chatMessageRepository.save(message);

        Conversation conversation = message.getConversation();
        conversation.setUnreadMessageCount(conversation.getUnreadMessageCount() );
    }

    @Override
    public void processOrderMessage(Message msg, Authentication principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (user.isEmpty()) {
            return;
        }

        Optional<Profile> from = profileRepository.findByIdAndUser(msg.getFromProfileId(), user.get());
        if (from.isEmpty()) {
            return;
        }

        Optional<Profile> to = profileRepository.findById(msg.getToProfileId());
        if (to.isEmpty()) {
            return;
        }

        Conversation conversation = getOrCreateConversation("ORDER-" + msg.getOrderId(), from.get(), to.get());

        ChatMessage message = new ChatMessage();
        message.setMessageIdentifier(UUID.randomUUID().toString());
        message.setContent(msg.getContent());
        message.setConversation(conversation);
        message.setOrderRequest(false);
        message.setRead(false);
        message.setReceiver(to.get());
        message.setSender(from.get());
        message = chatMessageRepository.save(message);

        messagingTemplate.convertAndSendToUser(conversation.getConversationName(), "/topic/order/conversation", message);

    }


    private Conversation getOrCreateConversation(Profile from, Profile to) {
        String conversationName = "%s-%d-%s-%d".formatted(from.getProfileType(), from.getId(), to.getProfileType(), to.getId());
        Optional<Conversation> optionalConversation = conversationRepository.findFirstByConversationName(conversationName);
        if (optionalConversation.isPresent()) {
            return optionalConversation.get();
        }

        Conversation conversation = new Conversation();
        conversation.setConversationName(conversationName);
        conversation.setParticipants(Set.of(from, to));
        conversation.setHasUnreadMessages(true);
        conversation.setType(ConversationType.INDIVIDUAL);
        conversation.setLastMessageTimestamp(LocalDateTime.now());
        conversation.setUnreadMessageCount(conversation.getUnreadMessageCount() + 1);
        return conversationRepository.save(conversation);
    }

    private Conversation getOrCreateConversation(String orderId, Profile from, Profile to) {
        Optional<Conversation> optionalConversation = conversationRepository.findFirstByConversationName(orderId);
        if (optionalConversation.isPresent()) {
            return optionalConversation.get();
        }

        Conversation conversation = new Conversation();
        conversation.setConversationName(orderId);
        conversation.setParticipants(Set.of(from, to));
        conversation.setHasUnreadMessages(true);
        conversation.setType(ConversationType.ORDER);
        conversation.setLastMessageTimestamp(LocalDateTime.now());
        conversation.setUnreadMessageCount(conversation.getUnreadMessageCount() + 1);
        return conversationRepository.save(conversation);
    }




}
