package com.ddd.dddapi.domain.chat.dto

import com.ddd.dddapi.common.enums.MessageType

data class RequestResponseChatMessage(
    val requestMessageType: MessageType,
    val requestMessage: String,
    val responseMessageType: MessageType,
    val responseMessage: String,
)
