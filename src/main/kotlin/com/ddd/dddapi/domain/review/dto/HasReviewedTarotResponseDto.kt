package com.ddd.dddapi.domain.review.dto

import io.swagger.v3.oas.annotations.media.Schema

class HasReviewedTarotResponseDto(
    @field:Schema(description = "후기 작셩여부", example = "true")
    val hasReviewed: Boolean,
)