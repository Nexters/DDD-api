package com.ddd.dddapi.domain.chat.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ChatRoomCreateResponseDto(
    @field:Schema(description = "생성한 채팅방의 ID", example = "12345")
    val roomId: Long,
    @field:Schema(description = "웰컴메세지 정보. 신규/기존 유저인지에 따라 메세지가 다름")
    val message: ChatMessageResponseDto?
)
