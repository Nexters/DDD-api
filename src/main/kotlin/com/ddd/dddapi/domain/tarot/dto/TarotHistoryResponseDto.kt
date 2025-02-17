package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.domain.tarot.entity.TarotHistoryEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class TarotHistoryResponseDto(
    @field:Schema(description = "히스토리 ID", example = "1")
    val id: Long,
    @field:Schema(description = "요약한 타로 질문", example = "우리 사이 어떻게 될까?")
    val questionSummary: String,
    @field:Schema(description = "사용자가 선택한 타로카드", example = "S_01")
    val selectedTarot: String,
    @field:Schema(description = "타로 결과 ID", example = "1")
    val tarotResultId: Long,
    @field:Schema(description = "타로 질문한 채팅방 ID", example = "1")
    val chatRoomId: Long,
    @field:Schema(description = "히스토리 생성일", example = "2021-08-01T00:00:00")
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(
            history: TarotHistoryEntity,
        ): TarotHistoryResponseDto {
            return TarotHistoryResponseDto(
                id = history.id,
                questionSummary = history.questionSummary,
                selectedTarot = history.tarotResult.tarot.name,
                tarotResultId = history.tarotResult.id,
                chatRoomId = history.chatRoom.id,
                createdAt = history.createdAt ?: LocalDateTime.now()
            )
        }
    }
}

