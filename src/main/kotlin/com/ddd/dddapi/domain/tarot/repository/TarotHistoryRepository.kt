package com.ddd.dddapi.domain.tarot.repository

import com.ddd.dddapi.domain.tarot.entity.TarotHistoryEntity
import com.ddd.dddapi.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface TarotHistoryRepository: JpaRepository<TarotHistoryEntity, Long> {
    @EntityGraph(attributePaths = ["chatRoom", "tarotResult"])
    fun findAllByUserOrderByCreatedAtDesc(user: UserEntity): List<TarotHistoryEntity>
}