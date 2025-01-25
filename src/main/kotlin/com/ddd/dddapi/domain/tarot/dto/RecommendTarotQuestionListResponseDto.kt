package com.ddd.dddapi.domain.tarot.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RecommendTarotQuestionListResponseDto(
    @field:Schema(description = "추천 질문 리스트")
    val questions: List<RecommendTarotQuestionResponseDto>
)
