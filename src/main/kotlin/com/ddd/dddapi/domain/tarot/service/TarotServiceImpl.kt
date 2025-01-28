package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.common.enums.TarotInfo
import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.domain.chat.dto.ChatMessageResponseDto
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.repository.TarotChatMessageRepository
import com.ddd.dddapi.domain.chat.service.ChatService
import com.ddd.dddapi.domain.chat.service.helper.ChatHelperService
import com.ddd.dddapi.domain.tarot.dto.RecommendTarotQuestionListResponseDto
import com.ddd.dddapi.domain.tarot.dto.RecommendTarotQuestionResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotResultResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotSelectRequestDto
import com.ddd.dddapi.domain.tarot.entity.TarotResultEntity
import com.ddd.dddapi.domain.tarot.repository.TarotQuestionRepository
import com.ddd.dddapi.domain.tarot.repository.TarotResultRepository
import com.ddd.dddapi.domain.tarot.service.helper.TarotHelperService
import com.ddd.dddapi.domain.user.service.UserService
import com.ddd.dddapi.domain.user.service.helper.UserHelperService
import com.ddd.dddapi.external.ai.client.AiClient
import com.ddd.dddapi.external.ai.dto.AiTarotResultRequestDto
import com.ddd.dddapi.external.ai.dto.AiTarotResultResponseDto
import org.springframework.data.domain.Limit
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TarotServiceImpl(
    private val aiClient: AiClient,
    private val userHelperService: UserHelperService,
    private val chatHelperService: ChatHelperService,
    private val tarotHelperService: TarotHelperService,
    private val tarotQuestionRepository: TarotQuestionRepository,
    private val tarotResultRepository: TarotResultRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository
): TarotService {
    @Transactional
    override fun selectTarot(tempUserKey: String, request: TarotSelectRequestDto): ChatMessageResponseDto {
        // validation
        val user = userHelperService.getUserOrThrow(tempUserKey)
        val room = chatHelperService.getChatRoomOrThrow(request.roomId, user)

        // tarotResult 생성
        val tarotResultInfo = aiClient.chatTarotResult(
            AiTarotResultRequestDto(room.id.toString(), request.tarotName)
        )

        // tarotResult 저장
        val tarotResult = createTarotResultEntity(tarotResultInfo, request.tarotName)
        val replyChatMessage = TarotChatMessageEntity.createTarotResultChatMessage(room, request.tarotName, tarotResult)
        tarotResultRepository.save(tarotResult)
        tarotChatMessageRepository.save(replyChatMessage)

        return ChatMessageResponseDto.of(replyChatMessage)
    }

    @Transactional
    override fun getTarotResult(tarotResultId: Long): TarotResultResponseDto {
        val tarotResult = tarotHelperService.getTarotResultOrThrow(tarotResultId)
        val tarotResultMessage = chatHelperService.getTarotResultMessageOrThrow(tarotResult)
        return TarotResultResponseDto.of(tarotResult, tarotResultMessage.message)
    }

    @Transactional
    override fun getRecommendTarotQuestions(): RecommendTarotQuestionListResponseDto {
        val questionList = tarotQuestionRepository.findByOrderByReferenceCountDesc(Limit.of(10))
            .shuffled().take(4)
        return RecommendTarotQuestionListResponseDto(
            questionList.map { RecommendTarotQuestionResponseDto.of(it) }
        )
    }

    private fun createTarotResultEntity(
        tarotResultInfo: AiTarotResultResponseDto,
        tarotName: TarotInfo
    ): TarotResultEntity =
        TarotResultEntity(
            tarot = tarotName,
            type = tarotResultInfo.type,
            cardValueSummary = tarotResultInfo.summaryOfDescriptionOfCard,
            cardValueDescription = tarotResultInfo.descriptionOfCard,
            answerSummary = tarotResultInfo.summaryOfAnalysis,
            answerDescription = tarotResultInfo.analysis,
            adviceSummary = tarotResultInfo.summaryOfAdvice,
            adviceDescription = tarotResultInfo.advice
        )
}