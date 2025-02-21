package com.ddd.dddapi.domain.tarot.repository

import com.ddd.dddapi.domain.tarot.entity.TarotResultFollowQuestionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TarotResultFollowQuestionRepository: JpaRepository<TarotResultFollowQuestionEntity, Long> {
    fun findByTarotResultId(tarotResultId: Long): List<TarotResultFollowQuestionEntity>
}