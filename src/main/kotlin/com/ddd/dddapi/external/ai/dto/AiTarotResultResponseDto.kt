package com.ddd.dddapi.external.ai.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class AiTarotResultResponseDto(
    val type: String,
    val summaryOfDescriptionOfCard: String,
    val descriptionOfCard: String,
    val summaryOfAnalysis: String,
    val analysis: String,
    val summaryOfAdvice: String,
    val advice: String,
)
