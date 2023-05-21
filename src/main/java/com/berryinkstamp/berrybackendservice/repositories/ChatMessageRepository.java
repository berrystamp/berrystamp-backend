package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.ChatMessage;
import com.berryinkstamp.berrybackendservice.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversation(Conversation conversation);

}