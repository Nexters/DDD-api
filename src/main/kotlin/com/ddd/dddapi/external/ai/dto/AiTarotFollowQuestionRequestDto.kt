package com.ddd.dddapi.external.ai.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AiTarotFollowQuestionRequestDto(
    val chatRoomId: String
)
