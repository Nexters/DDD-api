package com.ddd.dddapi.domain.chat.controller

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.dto.RequestUserInfo
import com.ddd.dddapi.domain.chat.dto.ChatMessageListResponseDto
import com.ddd.dddapi.domain.chat.dto.ChatMessageResponseDto
import com.ddd.dddapi.domain.chat.dto.ChatMessageSendRequestDto
import com.ddd.dddapi.domain.chat.dto.ChatRoomCreateResponseDto
import com.ddd.dddapi.domain.chat.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/chat")
@Tag(name = "01. Chat API")
class ChatController(
    private val chatService: ChatService
) {
    @Operation(summary = "채팅룸 생성", description = "해당 유저의 기존 채팅방 유무에 따라 다른 웰컴메세지로 응답합니다")
    @PostMapping("/room")
    fun createChatRoom(
        @RequestUser requestUser: RequestUserInfo,
    ): ChatRoomCreateResponseDto {
        return chatService.createChatRoom(requestUser.userKey)
    }

    @Operation(summary = "채팅룸 내 메세지 목록 가져오기", description = "해당 채팅방의 모든 메세지 목록을 가져옵니다")
    @GetMapping("/room/messages")
    fun getRoomMessages(
        @RequestUser requestUser: RequestUserInfo,
        @RequestParam roomId: Long
    ): ChatMessageListResponseDto {
        return chatService.getChatRoomMessages(requestUser.userKey, roomId)
    }

    @Operation(summary = "채팅룸에 메세지 보내기", description = "채팅방에 메세지를 보냅니다")
    @PostMapping("/room/message")
    fun sendMessage(
        @RequestUser requestUser: RequestUserInfo,
        @Valid @RequestBody request: ChatMessageSendRequestDto
    ): ChatMessageResponseDto {
        return chatService.sendChatMessage(requestUser.userKey, request)
    }
}