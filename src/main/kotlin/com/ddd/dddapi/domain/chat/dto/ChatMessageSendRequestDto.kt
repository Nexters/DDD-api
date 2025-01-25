package com.ddd.dddapi.domain.chat.dto

import com.ddd.dddapi.common.enums.MessageIntent
import io.swagger.v3.oas.annotations.media.Schema

data class ChatMessageSendRequestDto(
    @field:Schema(description = "채팅방 ID", example = "12345")
    val roomId: Long,
    @field:Schema(description = "보낼 메세지", example = "타로카드를 뽑기ㄱㄱ")
    val message: String,
    @field:Schema(description = "메세지의 의도", defaultValue = "NORMAL")
    val intent: MessageIntent = MessageIntent.NORMAL,
    @field:Schema(description = "intent = RECOMMEND_QUESTION 으로 메세지 보낼 시 해당 추천질문ID", example = "12345")
    val referenceQuestionId: Long? = null
)
