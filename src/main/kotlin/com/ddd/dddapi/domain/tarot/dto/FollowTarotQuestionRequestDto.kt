package com.ddd.dddapi.domain.tarot.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FollowTarotQuestionRequestDto(
    @field:Schema(description = "현재 참여중인 채팅방 ID", example = "9")
    val chatRoomId: Long
)