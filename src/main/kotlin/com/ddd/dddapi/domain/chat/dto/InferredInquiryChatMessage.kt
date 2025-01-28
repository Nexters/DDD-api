package com.ddd.dddapi.domain.chat.dto

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity
import com.ddd.dddapi.domain.user.entity.UserEntity

data class InferredInquiryChatMessage(
    val message: String,
    val messageType: MessageType,
    val referenceQuestionId: Long? = null,
    val senderType: MessageSender = MessageSender.USER,
) {
    fun toChatMessageEntity(chatRoom: TarotChatRoomEntity, user: UserEntity): TarotChatMessageEntity =
        TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = messageType,
            senderType = senderType,
            sender = user,
            message = message,
            referenceTarotQuestionId = referenceQuestionId,
        )
}
