package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.domain.tarot.entity.TarotHistoryEntity
import java.time.LocalDateTime

data class TarotHistoryResponseDto(
    val id: Long,
    val questionSummary: String,
    val selectedTarot: String,
    val tarotResultId: Long,
    val chatRoomId: Long,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(
            history: TarotHistoryEntity,
        ): TarotHistoryResponseDto {
            return TarotHistoryResponseDto(
                id = history.id,
                questionSummary = history.questionSummary,
                selectedTarot = history.selectedTarot.name,
                tarotResultId = history.tarotResult.id,
                chatRoomId = history.chatRoom.id,
                createdAt = history.createdAt ?: LocalDateTime.now()
            )
        }
    }
}

