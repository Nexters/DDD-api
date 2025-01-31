package com.ddd.dddapi.domain.tarot.repository

import com.ddd.dddapi.domain.tarot.entity.TarotQuestionEntity
import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository

interface TarotQuestionRepository: JpaRepository<TarotQuestionEntity, Long> {
    fun findByOrderByReferenceCountDesc(limit: Limit): List<TarotQuestionEntity>

    fun findByQuestion(question: String): TarotQuestionEntity?
}