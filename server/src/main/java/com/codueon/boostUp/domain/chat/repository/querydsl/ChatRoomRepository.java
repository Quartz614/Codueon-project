package com.codueon.boostUp.domain.chat.repository.querydsl;

import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Boolean existsBySenderIdAndReceiverId(Long firstId, Long secondId);
    List<ChatRoom> findChatRoomsBySenderIdOrReceiverId(Long senderId, Long receiverId);
}