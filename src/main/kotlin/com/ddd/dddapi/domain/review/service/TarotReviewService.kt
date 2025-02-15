package com.ddd.dddapi.domain.review.service

import com.ddd.dddapi.domain.review.enums.TarotReviewGrade

interface TarotReviewService {
    fun createTarotReview(userKey: String, tarotResultId:Long, grade: TarotReviewGrade)
    fun hasReviewedTarot(userKey: String, tarotResultId:Long): Boolean
}