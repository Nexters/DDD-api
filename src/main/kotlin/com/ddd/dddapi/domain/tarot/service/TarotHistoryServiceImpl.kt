package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.domain.chat.service.helper.ChatHelperService
import com.ddd.dddapi.domain.tarot.dto.TarotHistoryListResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotHistoryEventDto
import com.ddd.dddapi.domain.tarot.entity.TarotHistoryEntity
import com.ddd.dddapi.domain.tarot.repository.TarotHistoryRepository
import com.ddd.dddapi.domain.tarot.service.helper.TarotHelperService
import com.ddd.dddapi.domain.user.service.helper.UserHelperService
import com.ddd.dddapi.external.ai.client.AiClient
import com.ddd.dddapi.external.ai.dto.AiTarotQuestionSummaryRequestDto
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

private const val TAROT_HISTORY_EXECUTOR_NAME = "tarotHistoryExecutor"

@Service
class TarotHistoryServiceImpl(
    private val aiClient: AiClient,
    private val userHelperService: UserHelperService,
    private val chatHelperService: ChatHelperService,
    private val tarotHelperService: TarotHelperService,
    private val tarotHistoryRepository: TarotHistoryRepository
): TarotHistoryService {
    @Transactional
    override fun getTarotHistoryList(userKey: String): TarotHistoryListResponseDto {
        val user = userHelperService.getUserOrThrow(userKey)
        val historyList = tarotHistoryRepository.findAllByUserOrderByCreatedAtDesc(user)
        return TarotHistoryListResponseDto.of(historyList)
    }

    @Async(TAROT_HISTORY_EXECUTOR_NAME)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun saveTarotHistory(request: TarotHistoryEventDto) {
        val user = userHelperService.getUserOrThrow(request.userKey)
        val tarotResult = tarotHelperService.getTarotResultOrThrow(request.tarotResultId)
        val tarotResultMessage = chatHelperService.getTarotResultMessageOrThrow(tarotResult)

        val chatRoom = tarotResultMessage.chatRoom
        val recentTarotQuestion = chatHelperService.getLatestUserTarotQuestionOrThrow(chatRoom, tarotResult)
        val tarotQuestionSummary = aiClient.tarotQuestionSummary(
            AiTarotQuestionSummaryRequestDto(
                recentTarotQuestion.message
            )
        )

        val tarotHistory = TarotHistoryEntity(
            user = user,
            chatRoom = chatRoom,
            questionSummary = tarotQuestionSummary.summarizedQuestion,
            tarotResult = tarotResult
        )

        tarotHistoryRepository.save(tarotHistory)
    }
}