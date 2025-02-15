package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.domain.tarot.dto.TarotHistoryListResponseDto

interface TarotHistoryService {
    fun getTarotHistoryList(userKey: String): TarotHistoryListResponseDto
}