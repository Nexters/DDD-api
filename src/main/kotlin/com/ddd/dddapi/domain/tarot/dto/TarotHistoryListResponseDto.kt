package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.domain.tarot.entity.TarotHistoryEntity
import io.swagger.v3.oas.annotations.media.Schema

data class TarotHistoryListResponseDto(
    @field:Schema(description = "타로 히스토리 정보들")
    val results: List<TarotHistoryResponseDto>
) {
    companion object {
        fun of(
            results: List<TarotHistoryEntity>
        ): TarotHistoryListResponseDto {
            return TarotHistoryListResponseDto(
                results = results.map { TarotHistoryResponseDto.of(it) }
            )
        }
    }
}
