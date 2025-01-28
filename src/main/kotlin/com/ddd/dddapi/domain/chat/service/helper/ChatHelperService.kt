package com.ddd.dddapi.domain.chat.service.helper

import com.ddd.dddapi.common.exception.BadRequestBizException
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
        // TODO: 예외 구체화
        return tarotChatRoomRepository.findById(roomId)
            .orElseThrow { BadRequestBizException("채팅방이 존재하지 않습니다.") }
    }

    fun getChatRoomOrThrow(roomId: Long, user: UserEntity): TarotChatRoomEntity {
        // TODO: 예외 구체화
        return tarotChatRoomRepository.findByIdAndUser(roomId, user)
            ?: throw BadRequestBizException("해당 유저 권한의 채팅방이 존재하지 않습니다.")
    }

    fun getTarotResultMessageOrThrow(tarotResult: TarotResultEntity): TarotChatMessageEntity {
        // TODO: 예외 구체화
        return tarotChatMessageRepository.findByTarotResult(tarotResult)
            ?: throw BadRequestBizException("타로 결과를 담은 메세지를 찾을 수 없습니다.")
    }

    fun getLatestUserTarotQuestionOrThrow(roomId: Long): TarotChatMessageEntity {
        // TODO: 예외 구체화
        return tarotChatMessageRepository.findLatestUserTarotQuestion(roomId)
            ?: throw BadRequestBizException("현재 채팅방에 타로 질문이 존재하지 않습니다.")
    }
}