package com.ddd.dddapi.domain.review.service

import com.ddd.dddapi.domain.review.entity.TarotReviewEntity
import com.ddd.dddapi.domain.review.enums.TarotReviewGrade
import com.ddd.dddapi.domain.review.repository.TarotReviewRepository
import com.ddd.dddapi.domain.tarot.service.helper.TarotHelperService
import com.ddd.dddapi.domain.user.service.helper.UserHelperService
import org.springframework.stereotype.Service

@Service
class TarotReviewServiceImpl(
    private val tarotReviewRepository: TarotReviewRepository,
    private val tarotHelperService: TarotHelperService,
    private val userHelperService: UserHelperService,
) : TarotReviewService {
    override fun createTarotReview(userKey: String, tarotResultId: Long, grade: TarotReviewGrade) {
        val tarotResult = tarotHelperService.getTarotResultOrThrow(tarotResultId)
        val user = userHelperService.getUserOrThrow(userKey)
        tarotReviewRepository.save(
            TarotReviewEntity(
                tarotGrade = grade,
                tarotResult = tarotResult,
                user = user
            )
        )
    }

    override fun hasReviewedTarot(userKey: String, tarotResultId: Long): Boolean {
        val user = userHelperService.getUserOrThrow(userKey)
        return tarotReviewRepository.existsByUserIdAndTarotResultId(user.id, tarotResultId)
    }
}