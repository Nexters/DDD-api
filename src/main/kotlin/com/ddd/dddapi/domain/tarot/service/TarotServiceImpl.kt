package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.domain.chat.dto.ChatMessageResponseDto
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.repository.TarotChatMessageRepository
import com.ddd.dddapi.domain.chat.service.ChatService
import com.ddd.dddapi.domain.tarot.dto.RecommendTarotQuestionListResponseDto
import com.ddd.dddapi.domain.tarot.dto.RecommendTarotQuestionResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotResultResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotSelectRequestDto
import com.ddd.dddapi.domain.tarot.entity.TarotResultEntity
import com.ddd.dddapi.domain.tarot.repository.TarotQuestionRepository
import com.ddd.dddapi.domain.tarot.repository.TarotResultRepository
import com.ddd.dddapi.domain.user.service.UserService
import com.ddd.dddapi.external.ai.client.AiClient
import com.ddd.dddapi.external.ai.dto.AiTarotResultRequestDto
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TarotServiceImpl(
    private val userService: UserService,
    private val chatService: ChatService,
    private val aiClient: AiClient,
    private val tarotQuestionRepository: TarotQuestionRepository,
    private val tarotResultRepository: TarotResultRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository
): TarotService {
    @Transactional
    override fun selectTarot(tempUserKey: String, request: TarotSelectRequestDto): ChatMessageResponseDto {
        val user = userService.getUserOrThrow(tempUserKey)
        val room = chatService.getChatRoomOrThrow(request.roomId)
        val tarotQuestion = chatService.getLatestUserTarotQuestionOrThrow(request.roomId)

        val tarotResultInfo = aiClient.chatTarotResult(
            AiTarotResultRequestDto(
                chatRoomId = room.id.toString(),
                chat = tarotQuestion.message,
                tarotCard = request.tarotName
            )
        )

        val tarotResult = TarotResultEntity(
            relatedQuestionMessage = tarotQuestion,
            tarot = request.tarotName,
            type = tarotResultInfo.type,
            cardValueSummary = tarotResultInfo.summaryOfDescriptionOfCard,
            cardValueDescription = tarotResultInfo.descriptionOfCard,
            answerSummary = tarotResultInfo.summaryOfAnalysis,
            answerDescription = tarotResultInfo.analysis,
            adviceSummary = tarotResultInfo.summaryOfAdvice,
            adviceDescription = tarotResultInfo.advice
        ).also { tarotResultRepository.save(it) }
        val newChatMessage = TarotChatMessageEntity(
            chatRoom = room,
            messageType = MessageType.SYSTEM_TAROT_RESULT,
            senderType = MessageSender.SYSTEM,
            message = "타로 결과를 다시 보고 싶다면 카드를 눌러보라\n또 궁금한거 있어냥?",
            tarotResult = tarotResult
        ).also { tarotChatMessageRepository.save(it) }

        return ChatMessageResponseDto.fromChatMessageEntity(newChatMessage)
    }

    @Transactional
    override fun getTarotResult(tarotResultId: Long): TarotResultResponseDto {
        val tarotResult = tarotResultRepository.findById(tarotResultId)
            .orElseThrow { BadRequestBizException("타로 결과를 찾을 수 없습니다.") }
        return TarotResultResponseDto.of(tarotResult)
    }

    @Transactional
    override fun getRecommendTarotQuestions(): RecommendTarotQuestionListResponseDto {
        val questionList = tarotQuestionRepository.findByOrderByReferenceCountDesc(Limit.of(10))
            .shuffled().take(4)
        return RecommendTarotQuestionListResponseDto(
            questionList.map { RecommendTarotQuestionResponseDto.of(it) }
        )
    }
}