package com.ddd.dddapi.domain.chat.repository

import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity
import com.ddd.dddapi.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TarotChatRoomRepository: JpaRepository<TarotChatRoomEntity, Long> {
    fun findByUser(user: UserEntity): List<TarotChatRoomEntity>
}