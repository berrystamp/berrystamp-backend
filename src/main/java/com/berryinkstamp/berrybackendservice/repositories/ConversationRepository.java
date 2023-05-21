package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.Conversation;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("select c from Conversation c where c.participants in ?1 order by c.lastMessageTimestamp DESC")
    List<Conversation> findByParticipantsInOrderByLastMessageTimestampDesc(Collection<Profile> participants);

    Optional<Conversation> findFirstByConversationName(String conversationName);



}