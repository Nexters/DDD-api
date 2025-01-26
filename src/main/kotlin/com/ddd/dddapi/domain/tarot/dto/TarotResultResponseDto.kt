package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.common.enums.TarotInfo
import com.ddd.dddapi.domain.tarot.entity.TarotResultEntity
import io.swagger.v3.oas.annotations.media.Schema

data class TarotResultResponseDto(
    @field:Schema(description = "사용자가 선택한 타로카드", example = "S_01")
    val tarot: TarotInfo,
    @field:Schema(description = "질문 카테고리", example = "연애")
    val type: String,
    @field:Schema(description = "카드의 의미")
    val cardValue: CardValue,
    @field:Schema(description = "질문에 대한 대답")
    val answer: Answer,
    @field:Schema(description = "조언")
    val advice: Advice
) {
    companion object {
        fun of(
            tarotResultEntity: TarotResultEntity,
            questionMessage: String
        ): TarotResultResponseDto {
            return TarotResultResponseDto(
                tarot = tarotResultEntity.tarot,
                type = tarotResultEntity.type,
                cardValue = CardValue(
                    summary = tarotResultEntity.cardValueSummary,
                    description = tarotResultEntity.cardValueDescription
                ),
                answer = Answer(
                    summary = tarotResultEntity.answerSummary,
                    description = tarotResultEntity.answerDescription,
                    question = questionMessage
                ),
                advice = Advice(
                    summary = tarotResultEntity.adviceSummary,
                    description = tarotResultEntity.adviceDescription
                )
            )
        }
    }

    data class CardValue(
        val summary: String,
        val description: String
    )

    data class Answer(
        val summary: String,
        val description: String,
        val question: String
    )

    data class Advice(
        val summary: String,
        val description: String
    )
}