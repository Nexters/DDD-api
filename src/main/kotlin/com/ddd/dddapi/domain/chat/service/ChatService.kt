package com.ddd.dddapi.domain.chat.service

import com.ddd.dddapi.domain.chat.dto.ChatMessageListResponseDto
import com.ddd.dddapi.domain.chat.dto.ChatMessageResponseDto
import com.ddd.dddapi.domain.chat.dto.ChatMessageSendRequestDto
import com.ddd.dddapi.domain.chat.dto.ChatRoomCreateResponseDto
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.chat.entity.TarotChatRoomEntity

interface ChatService {
    fun createChatRoom(tempUserKey: String): ChatRoomCreateResponseDto
    fun getChatRoomMessages(tempUserKey: String, roomId: Long): ChatMessageListResponseDto
    fun sendChatMessage(tempUserKey: String, request: ChatMessageSendRequestDto): ChatMessageResponseDto
    fun getChatRoomOrThrow(roomId: Long): TarotChatRoomEntity
    fun getLatestUserTarotQuestionOrThrow(roomId: Long): TarotChatMessageEntity
}