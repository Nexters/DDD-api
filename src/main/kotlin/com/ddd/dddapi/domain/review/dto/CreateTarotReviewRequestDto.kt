package com.ddd.dddapi.domain.review.dto

import com.ddd.dddapi.domain.review.enums.TarotReviewGrade

class CreateTarotReviewRequestDto(
    val tarotResultId: Long,
    val grade: TarotReviewGrade,
)