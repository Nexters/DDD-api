package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.domain.tarot.dto.TarotHistoryListResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotHistoryEventDto

interface TarotHistoryService {
    fun getTarotHistoryList(userKey: String): TarotHistoryListResponseDto

    fun saveTarotHistory(request: TarotHistoryEventDto)
}