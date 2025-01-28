package com.ddd.dddapi.domain.chat.dto

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType

data class InferredInquiryChatMessage(
    val messageType: MessageType,
    val senderType: MessageSender = MessageSender.USER,
    val message: String,
    val referenceQuestionId: Long?
)
