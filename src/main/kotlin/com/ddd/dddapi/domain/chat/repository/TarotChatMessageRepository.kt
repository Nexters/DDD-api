package com.ddd.dddapi.domain.chat.repository

import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TarotChatMessageRepository: JpaRepository<TarotChatMessageEntity, Long> {
    fun findAllByChatRoom(chatRoom: TarotChatRoomEntity): List<TarotChatMessageEntity>
    @Query(
        """
        SELECT m 
        FROM TarotChatMessageEntity m 
        WHERE m.chatRoom.id = :roomId AND  m.messageType = :messageType 
        ORDER BY m.createdAt DESC
        """
    )
    fun findLatestUserTarotQuestion(roomId: Long, messageType: MessageType = MessageType.USER_TAROT_QUESTION): TarotChatMessageEntity?
}