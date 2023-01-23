package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.ChatMessageRepository;
import com.codueon.boostUp.domain.chat.repository.redis.LastSentScoreRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final LastSentScoreRepository lastSentScoreRepository;

    private final String NOTHING_LEFT_TO_SEND = "NOTHING_LEFT_TO_SEND";
    private final String RDB_PREFIX = "index";
    private final String CHATROOM_PREFIX = "ChatRoom";
    private final String SESSION_PREFIX = "SessionId";

    /**
     * 메시지 객체(RedisChat) 생성 및 Redis 저장 메서드
     * @param message 메시지 정보
     * @param user Authentication User 정보
     * @return RedisChat
     * @author mozzi327
     */
    @Transactional
    public RedisChat setRedisChatInfo(PostMessage message, Long memberId) {
        RedisChat createChat = RedisChat.builder()
                .senderId(memberId)
                .chatRoomId(message.getChatRoomId())
                .createdAt(LocalDateTime.now())
                .message(message.getMessageContent())
                .build();
        chatMessageRepository.saveChatMessage(createChat);
        return createChat;
    }

    /**
     * 채팅 메시지 조회 메서드
     * @param chatRoomId 채팅방 식별자
     * @param sessionId 세션 식별자
     * @return List(RedisChat)
     * @author mozzi327
     */
    public List<RedisChat> getChatMessages(Long chatRoomId, String sessionId) {
        String scoreAsString = lastSentScoreRepository // LastSentScore에서 해당 메시지의 스코어를 조회
                .getLastSentScore(makeLastSentKey(chatRoomId, sessionId));
        isNothingToLeftToSEnd(scoreAsString); // 스코어 조회 유무 확인(메시지 내용이 없을 때 null)

        if (isRdbRetrieving(scoreAsString)) { // Redis에서의 채팅리스트 조회
            Long lastSentIndex = Long.parseLong(scoreAsString.replace(RDB_PREFIX, ""));
            return getMessagesFromRdb(chatRoomId, lastSentIndex - 1L, sessionId);
        }
        double lastSentScore = getNextScoreOfRecentlySent(scoreAsString); //
        Double oldestScoreToSend = chatMessageRepository // Redis에서 제일 오래된 메시지의 스코어를 반환
                .getScoreOfLastChatWithLimitN(chatRoomId, lastSentScore);

        if (oldestScoreToSend == null) //
            return getMessageRdbFirstTime(chatRoomId, lastSentScore, sessionId);

        // Redis에 제일 최근 메시지를 저장한 후,
        lastSentScoreRepository.saveLastSentScore(makeLastSentKey(chatRoomId, sessionId), oldestScoreToSend.toString());
        return chatMessageRepository.getMessagesFromRoomByScore(chatRoomId, oldestScoreToSend, lastSentScore);
    }

    private List<RedisChat> getMessagesFromRdb(Long chatRoomId, long lastSentIndex, String sessionId) {
        return null;
    }

    private List<RedisChat> getMessageRdbFirstTime(Long chatRoomId,
                                                   double lastSentScore,
                                                   String sessionId) {
        return null;
    }

    private double getNextScoreOfRecentlySent(String scoreAsString) {
        if (scoreAsString == null) return Double.MAX_VALUE;
        return Double.parseDouble(scoreAsString) -1L;
    }

    private boolean isRdbRetrieving(String scoreAsString) {
        return scoreAsString != null && scoreAsString.startsWith(RDB_PREFIX);
    }

    private void isNothingToLeftToSEnd(String scoreAsString) {
        if (scoreAsString != null && scoreAsString.startsWith(NOTHING_LEFT_TO_SEND)) return;
        throw new BusinessLogicException(ExceptionCode.CHATROOM_NOT_FOUND);
    }

    private String makeLastSentKey(Long chatRoomId, String sessionId) {
        return CHATROOM_PREFIX + chatRoomId + SESSION_PREFIX + sessionId;
    }
}