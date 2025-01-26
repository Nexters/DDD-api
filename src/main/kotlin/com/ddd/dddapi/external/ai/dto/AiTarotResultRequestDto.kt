package com.ddd.dddapi.external.ai.dto

import com.ddd.dddapi.common.enums.TarotInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AiTarotResultRequestDto(
    val chatRoomId: String,
    val tarotCard: TarotInfo
)