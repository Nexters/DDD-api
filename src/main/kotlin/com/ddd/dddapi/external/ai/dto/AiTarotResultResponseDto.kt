package com.ddd.dddapi.external.ai.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AiTarotResultResponseDto(
    val type: String,
    val summaryOfDescriptionOfCard: String = "카드의 의미",
    val descriptionOfCard: String,
    val summaryOfAnalysis: String = "카드의 답변",
    val analysis: String,
    val summaryOfAdvice: String = "타로냥의 조언",
    val advice: String,
    val comprehensiveSummary: String
)
