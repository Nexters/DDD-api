package com.ddd.dddapi.domain.review.dto

import com.ddd.dddapi.domain.review.enums.TarotReviewGrade
import io.swagger.v3.oas.annotations.media.Schema

class CreateTarotReviewRequestDto(
    @field:Schema(description = "타로결과 ID", example = "12345")
    val tarotResultId: Long,
    @field:Schema(description = "평가", example = "GOOD")
    val grade: TarotReviewGrade,
)