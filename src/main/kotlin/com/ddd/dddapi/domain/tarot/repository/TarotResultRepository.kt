package com.ddd.dddapi.domain.tarot.repository

import com.ddd.dddapi.domain.tarot.entity.TarotResultEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TarotResultRepository: JpaRepository<TarotResultEntity, Long> {
}