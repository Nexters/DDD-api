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
import com.ddd.dddapi.domain.tarot.entity.TarotQuestionEntity
import com.ddd.dddapi.domain.tarot.repository.TarotQuestionRepository
import com.ddd.dddapi.domain.user.service.UserService
import com.ddd.dddapi.external.ai.client.AiClient
import com.ddd.dddapi.external.ai.dto.AiChatCommonRequestDto
import com.ddd.dddapi.external.ai.dto.AiInferredChatType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatServiceImpl(
    private val userService: UserService,
    private val aiClient: AiClient,
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
        val user = userService.getUserOrThrow(tempUserKey)
        val chatRoom = getChatRoomOrThrow(roomId)
        val chatMessageList = tarotChatMessageRepository.findAllByChatRoom(chatRoom)
            .map { ChatMessageResponseDto.fromChatMessageEntity(it) }

        return ChatMessageListResponseDto(chatMessageList)
    }

    @Transactional
    override fun sendChatMessage(tempUserKey: String, request: ChatMessageSendRequestDto): ChatMessageResponseDto {
        val user = userService.getUserOrThrow(tempUserKey)
        val chatRoom = getChatRoomOrThrow(request.roomId)
        val requestResponseChatMessage = createRequestResponseChatMessage(chatRoom, request)

        val requestChatMessage = TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = requestResponseChatMessage.requestMessageType,
            senderType = MessageSender.USER,
            sender = user,
            message = requestResponseChatMessage.requestMessage,
            referenceTarotQuestionId = request.referenceQuestionId
        ).apply { tarotChatMessageRepository.save(this) }
        val responseChatMessage = TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = requestResponseChatMessage.responseMessageType,
            senderType = MessageSender.SYSTEM,
            message = requestResponseChatMessage.responseMessage
        ).apply { tarotChatMessageRepository.save(this) }

        return ChatMessageResponseDto.fromChatMessageEntity(responseChatMessage)
    }

    override fun getChatRoomOrThrow(roomId: Long): TarotChatRoomEntity {
        // TODO: 예외 구체화
        return tarotChatRoomRepository.findById(roomId)
            .orElseThrow { BadRequestBizException("채팅방이 존재하지 않습니다.") }
    }

    override fun getLatestUserTarotQuestionOrThrow(roomId: Long): TarotChatMessageEntity {
        // TODO: 예외 구체화
        return tarotChatMessageRepository.findLatestUserTarotQuestion(roomId)
            ?: throw BadRequestBizException("현재 채팅방에 타로 질문이 존재하지 않습니다.")
    }

    private fun createRequestResponseChatMessage(
        chatRoom: TarotChatRoomEntity,
        request: ChatMessageSendRequestDto
    ): RequestResponseChatMessage =
        when(request.intent) {
            MessageIntent.NORMAL -> {
                val chatClassification = aiClient.chatClassification(AiChatCommonRequestDto(chatRoom.id.toString(), request.message))
                val responseMessage = getResponseChatByClassification(chatRoom.id, request.message, chatClassification.type)
                RequestResponseChatMessage(
                    requestMessage = request.message,
                    requestMessageType = MessageType.userMessageFrom(chatClassification.type),
                    responseMessage = responseMessage,
                    responseMessageType = MessageType.systemMessageFrom(chatClassification.type)
                )
            }
            MessageIntent.TAROT_ACCEPT -> {
                val responseMessage = "너의 고민에 집중하면서\n카드를 한 장 뽑아봐."
                RequestResponseChatMessage(
                    requestMessage = request.message,
                    requestMessageType = MessageType.USER_TAROT_QUESTION_ACCEPTANCE,
                    responseMessage = responseMessage,
                    responseMessageType = MessageType.SYSTEM_TAROT_QUESTION_ACCEPTANCE_REPLY
                )
            }
            MessageIntent.TAROT_DECLINE -> {
                val responseMessage = aiClient.chatCasually(AiChatCommonRequestDto(chatRoom.id.toString(), request.message)).answer
                RequestResponseChatMessage(
                    requestMessage = request.message,
                    requestMessageType = MessageType.USER_TAROT_QUESTION_DECLINE,
                    responseMessage = responseMessage,
                    responseMessageType = MessageType.SYSTEM_NORMAL_REPLY
                )
            }
            MessageIntent.RECOMMEND_QUESTION -> {
                val referenceQuestionId = request.referenceQuestionId ?: throw BadRequestBizException("참조 질문이 없습니다.")
                tarotQuestionRepository.findById(referenceQuestionId)
                    .orElseThrow { BadRequestBizException("참조 질문이 존재하지 않습니다.") }
                    .apply { referenceCount += 1 }

                val responseMessage = "좋아! 너의 고민에 집중하면서\n카드를 한 장 뽑아봐."
                RequestResponseChatMessage(
                    requestMessage = request.message,
                    requestMessageType = MessageType.USER_TAROT_QUESTION_ACCEPTANCE,
                    responseMessage = responseMessage,
                    responseMessageType = MessageType.SYSTEM_TAROT_QUESTION_ACCEPTANCE_REPLY
                )
            }
        }

    private fun getResponseChatByClassification(
        roomId: Long,
        message: String,
        chatType: AiInferredChatType
    ): String =
        when(chatType) {
            AiInferredChatType.GENERAL -> aiClient.chatCasually(AiChatCommonRequestDto(roomId.toString(), message)).answer
            AiInferredChatType.INAPPROPRIATE -> aiClient.chatInappropriate(AiChatCommonRequestDto(roomId.toString(), message)).answer
            AiInferredChatType.TAROT -> {
                tarotQuestionRepository.save(TarotQuestionEntity(question = message))
                aiClient.chatCasually(AiChatCommonRequestDto(roomId.toString(), message)).answer
            }
        }
}