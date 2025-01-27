package com.ddd.dddapi.domain.chat.service

import com.ddd.dddapi.common.enums.MessageIntent
import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.domain.chat.dto.*
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity
import com.ddd.dddapi.domain.chat.repository.TarotChatMessageRepository
import com.ddd.dddapi.domain.chat.repository.TarotChatRoomRepository
import com.ddd.dddapi.domain.chat.service.helper.ChatHelperService
import com.ddd.dddapi.domain.tarot.entity.TarotQuestionEntity
import com.ddd.dddapi.domain.tarot.repository.TarotQuestionRepository
import com.ddd.dddapi.domain.user.service.UserService
import com.ddd.dddapi.domain.user.service.helper.UserHelperService
import com.ddd.dddapi.external.ai.client.AiClient
import com.ddd.dddapi.external.ai.dto.AiChatCommonRequestDto
import com.ddd.dddapi.external.ai.dto.AiInferredChatType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatServiceImpl(
    private val aiClient: AiClient,
    private val userService: UserService,
    private val userHelperService: UserHelperService,
    private val tarotChatHelperService: ChatHelperService,
    private val tarotChatRoomRepository: TarotChatRoomRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository,
    private val tarotQuestionRepository: TarotQuestionRepository
): ChatService {
    @Transactional
    override fun createChatRoom(tempUserKey: String): ChatRoomCreateResponseDto {
        val user = userService.getOrCreateUser(tempUserKey)
        val newChatRoom = TarotChatRoomEntity(user = user)
        tarotChatRoomRepository.save(newChatRoom)

        return ChatRoomCreateResponseDto(
            roomId = newChatRoom.id,
            message = null
        )
    }

    @Transactional
    override fun getChatRoomMessages(tempUserKey: String, roomId: Long): ChatMessageListResponseDto {
        val user = userHelperService.getUserOrThrow(tempUserKey)
        val chatRoom = tarotChatHelperService.getChatRoomOrThrow(roomId)
        val chatMessageList = tarotChatMessageRepository.findAllByChatRoom(chatRoom)

        return ChatMessageListResponseDto(
            chatMessageList.map { ChatMessageResponseDto.of(it) }
        )
    }

    @Transactional
    override fun sendChatMessage(tempUserKey: String, request: ChatMessageSendRequestDto): ChatMessageResponseDto {
        val user = userHelperService.getUserOrThrow(tempUserKey)
        val chatRoom = tarotChatHelperService.getChatRoomOrThrow(request.roomId)

        // 둘로 나눌까? inquiry & reply
        // inquiry 판단. to 내부객체로 InferredInquiryChatMessage
        // reply 판단(inquiry 사용). to 내부객체로
        // DB 작업()
        // 응답

        val requestResponseChatMessage = createRequestResponseChatMessage(chatRoom, request)

        val requestChatMessage = TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = requestResponseChatMessage.requestMessageType,
            senderType = MessageSender.USER,
            sender = user,
            message = requestResponseChatMessage.requestMessage,
            referenceTarotQuestionId = request.referenceQuestionId
        )
        val responseChatMessage = TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = requestResponseChatMessage.responseMessageType,
            senderType = MessageSender.SYSTEM,
            message = requestResponseChatMessage.responseMessage
        )
        tarotChatMessageRepository.save(requestChatMessage)
        tarotChatMessageRepository.save(responseChatMessage)

        return ChatMessageResponseDto.of(responseChatMessage)
    }

    private fun createRequestResponseChatMessage(
        chatRoom: TarotChatRoomEntity,
        request: ChatMessageSendRequestDto
    ): RequestResponseChatMessage = when(request.intent) {
        MessageIntent.NORMAL -> {
            // AI 대화 유형 분류 & AI 답변
            val chatClassification = aiClient.chatClassification(
                AiChatCommonRequestDto(
                    chatRoom.id.toString(),
                    request.message
                )
            )
            val responseMessage = getReplyMessageByClassification(chatRoom.id, request.message, chatClassification.type)
            RequestResponseChatMessage(
                requestMessage = request.message,
                requestMessageType = MessageType.userMessageFrom(chatClassification.type),
                responseMessage = responseMessage,
                responseMessageType = MessageType.systemMessageFrom(chatClassification.type)
            )
        }
        MessageIntent.RECOMMEND_QUESTION -> {
            // 타로질문 AI 답변
            val referenceQuestionId = request.referenceQuestionId ?: throw BadRequestBizException("참조 질문이 없습니다.")
            tarotQuestionRepository.findById(referenceQuestionId)
                .orElseThrow { BadRequestBizException("참조 질문이 존재하지 않습니다.") }
                .apply { referenceCount += 1 }
            val responseMessage = aiClient.chatTarotQuestion(
                AiChatCommonRequestDto(
                    request.roomId.toString(),
                    request.message
                )
            ).answer
            RequestResponseChatMessage(
                requestMessage = request.message,
                requestMessageType = MessageType.USER_TAROT_QUESTION,
                responseMessage = responseMessage,
                responseMessageType = MessageType.SYSTEM_TAROT_QUESTION_REPLY
            )
        }
        MessageIntent.TAROT_DECLINE -> {
            // 일반질문 AI 답변
            val responseMessage = aiClient.chatCasually(
                AiChatCommonRequestDto(
                    chatRoom.id.toString(),
                    request.message
                )
            ).answer
            RequestResponseChatMessage(
                requestMessage = request.message,
                requestMessageType = MessageType.USER_TAROT_QUESTION_DECLINE,
                responseMessage = responseMessage,
                responseMessageType = MessageType.SYSTEM_NORMAL_REPLY
            )
        }
        MessageIntent.TAROT_ACCEPT -> {
            // 타로 선택 권유
            val responseMessage = "너의 고민에 집중하면서\n카드를 한 장 뽑아봐."
            RequestResponseChatMessage(
                requestMessage = request.message,
                requestMessageType = MessageType.USER_TAROT_QUESTION_ACCEPTANCE,
                responseMessage = responseMessage,
                responseMessageType = MessageType.SYSTEM_TAROT_QUESTION_ACCEPTANCE_REPLY
            )
        }
    }

    // TODO: 추론된 Inquiry 기준으로 답변얻기
    private fun getReplyMessageByClassification(
        roomId: Long,
        message: String,
        chatType: AiInferredChatType
    ): String = when(chatType) {
        AiInferredChatType.GENERAL -> aiClient.chatCasually(AiChatCommonRequestDto(roomId.toString(), message)).answer
        AiInferredChatType.INAPPROPRIATE -> aiClient.chatInappropriate(AiChatCommonRequestDto(roomId.toString(), message)).answer
        AiInferredChatType.TAROT -> {
            tarotQuestionRepository.save(TarotQuestionEntity(question = message))
            aiClient.chatTarotQuestion(AiChatCommonRequestDto(roomId.toString(), message)).answer
        }
    }


}