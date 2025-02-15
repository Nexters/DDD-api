package com.ddd.dddapi.domain.review.repository

import com.ddd.dddapi.domain.review.entity.TarotReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TarotReviewRepository: JpaRepository<TarotReviewEntity, Long> {
}