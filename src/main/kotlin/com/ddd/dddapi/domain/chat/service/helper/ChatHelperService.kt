package com.ddd.dddapi.domain.chat.service.helper

import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.common.exception.ForbiddenBizException
import com.ddd.dddapi.common.exception.InternalServerErrorBizException
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity
import com.ddd.dddapi.domain.chat.repository.TarotChatMessageRepository
import com.ddd.dddapi.domain.chat.repository.TarotChatRoomRepository
import com.ddd.dddapi.domain.common.annotation.HelperService
import com.ddd.dddapi.domain.tarot.entity.TarotResultEntity
import com.ddd.dddapi.domain.user.entity.UserEntity
import org.springframework.transaction.annotation.Transactional

@HelperService
@Transactional(readOnly = true)
class ChatHelperService(
    private val tarotChatRoomRepository: TarotChatRoomRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository
) {
    fun getChatRoomOrThrow(roomId: Long): TarotChatRoomEntity {
        return tarotChatRoomRepository.findById(roomId)
            .orElseThrow { BadRequestBizException("채팅방이 존재하지 않습니다. [id: $roomId]") }
    }

    fun getChatRoomOrThrow(roomId: Long, user: UserEntity): TarotChatRoomEntity {
        return tarotChatRoomRepository.findByIdAndUser(roomId, user)
            ?: throw ForbiddenBizException("해당 유저에게 채팅방 접근권한이 존재하지 않습니다. [userId: ${user.id}, roomId: $roomId]")
    }

    fun getTarotResultMessageOrThrow(tarotResult: TarotResultEntity): TarotChatMessageEntity {
        return tarotChatMessageRepository.findByTarotResult(tarotResult)
            ?: throw InternalServerErrorBizException("타로 결과 ID를 담은 메세지를 찾을 수 없습니다. [tarotResultId: ${tarotResult.id}]")
    }

    fun getLatestUserTarotQuestionOrThrow(roomId: Long): TarotChatMessageEntity {
        return tarotChatMessageRepository.findLatestUserTarotQuestion(roomId)
            ?: throw BadRequestBizException("현재 채팅방에 타로 질문이 존재하지 않습니다. [roomId: $roomId]")
    }
}