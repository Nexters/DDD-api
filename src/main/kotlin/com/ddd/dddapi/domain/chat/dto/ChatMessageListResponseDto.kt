package com.ddd.dddapi.domain.chat.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ChatMessageListResponseDto(
    @field:Schema(description = "채팅방에 존재하는 모든 메세지들의 정보")
    val messages: List<ChatMessageResponseDto>
)
