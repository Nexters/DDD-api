package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.domain.tarot.entity.TarotQuestionEntity
import io.swagger.v3.oas.annotations.media.Schema

data class RecommendTarotQuestionResponseDto(
    @field:Schema(description = "추천 질문 ID", example = "12345")
    val recommendQuestionId: Long,
    @field:Schema(description = "추천 질문", example = "오늘의 운세를 알려줘")
    val question: String,
    @field:Schema(description = "조회수", example = "123")
    val referenceCount: Long = 1,
) {
    companion object {
        fun of(
            tarotQuestionEntity: TarotQuestionEntity
        ): RecommendTarotQuestionResponseDto {
            return RecommendTarotQuestionResponseDto(
                recommendQuestionId = tarotQuestionEntity.id,
                question = tarotQuestionEntity.question,
                referenceCount = tarotQuestionEntity.referenceCount
            )
        }
    }
}
