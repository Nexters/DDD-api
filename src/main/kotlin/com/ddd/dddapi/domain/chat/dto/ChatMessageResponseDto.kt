package com.ddd.dddapi.domain.chat.dto

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.common.enums.TarotInfo
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import io.swagger.v3.oas.annotations.media.Schema

data class ChatMessageResponseDto(
    @field:Schema(description = "메시지 ID", example = "1")
    val messageId: Long,
    @field:Schema(description = "응답 메세지의 유형", example = "SYSTEM_NORMAL_REPLY")
    val type: MessageType,
    @field:Schema(description = "메시지를 보낸 사람", example = "USER")
    val sender: MessageSender,
    @field:Schema(description = "메시지 내용", example = "[\"안녕이다냥\", \"할말있냐냥?\" ]")
    val answers: List<String>,
    @field:Schema(description = "타로 이름. 타로 결과 메세지일때만 존재", example = "S_01")
    val tarotName: TarotInfo?,
    @field:Schema(description = "타로결과정보 ID. 타로 결과 메세지일때만 존재", example = "123")
    val tarotResultId: Long?
) {
    companion object {
        fun fromChatMessageEntity(
            chatMessage: TarotChatMessageEntity
        ): ChatMessageResponseDto {
            return ChatMessageResponseDto(
                messageId = chatMessage.id,
                type = chatMessage.messageType,
                sender = chatMessage.senderType,
                answers = chatMessage.message
                    .split("\n")
                    .filter { it.isNotBlank() }
                    .map { it.trim() },
                tarotName = chatMessage.tarot,
                tarotResultId = chatMessage.tarotResult?.id
            )
        }
    }
}
