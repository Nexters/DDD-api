package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.domain.tarot.dto.TarotHistoryListResponseDto
import com.ddd.dddapi.domain.tarot.repository.TarotHistoryRepository
import com.ddd.dddapi.domain.user.service.helper.UserHelperService
import org.springframework.stereotype.Service


@Service
class TarotHistoryServiceImpl(
    private val userHelperService: UserHelperService,
    private val tarotHistoryRepository: TarotHistoryRepository
): TarotHistoryService {
    override fun getTarotHistoryList(userKey: String): TarotHistoryListResponseDto {
        val user = userHelperService.getUserOrThrow(userKey)
        val historyList = tarotHistoryRepository.findAllByUser(user)
        return TarotHistoryListResponseDto.of(historyList)
    }
}