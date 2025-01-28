package com.ddd.dddapi.domain.chat.dto

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity

data class InferredReplyChatMessage(
    val message: String,
    val messageType: MessageType,
    val senderType: MessageSender = MessageSender.SYSTEM
) {
    fun toChatMessageEntity(chatRoom: TarotChatRoomEntity): TarotChatMessageEntity =
        TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = messageType,
            senderType = senderType,
            message = message,
        )
}
