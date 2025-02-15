package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.domain.tarot.entity.TarotHistoryEntity

data class TarotHistoryListResponseDto(
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
