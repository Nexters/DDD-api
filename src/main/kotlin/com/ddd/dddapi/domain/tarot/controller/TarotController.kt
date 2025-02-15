package com.ddd.dddapi.domain.tarot.controller

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.annotation.UserKeyIgnored
import com.ddd.dddapi.common.dto.RequestUserInfo
import com.ddd.dddapi.domain.chat.dto.ChatMessageResponseDto
import com.ddd.dddapi.domain.tarot.dto.RecommendTarotQuestionListResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotHistoryListResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotResultResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotSelectRequestDto
import com.ddd.dddapi.domain.tarot.service.TarotHistoryService
import com.ddd.dddapi.domain.tarot.service.TarotService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/tarot")
@Tag(name = "02. Tarot API")
class TarotController(
    private val tarotService: TarotService,
    private val tarotHistoryService: TarotHistoryService
) {
    @Operation(summary = "타로 카드 선택", description = "타로 결과 id를 담은 채팅메세지로 응답합니다.")
    @PostMapping("/select")
    fun selectTarot(
        @RequestUser requestUser: RequestUserInfo,
        @Valid @RequestBody request: TarotSelectRequestDto
    ): ChatMessageResponseDto {
        return tarotService.selectTarot(requestUser.userKey, request)
    }

    @Operation(summary = "타로 결과 가져오기", description = "타로 결과 id를 통해 타로 결과를 가져옵니다.")
    @UserKeyIgnored
    @GetMapping("/result/{resultId}")
    fun getTarotResult(
        @RequestUser requestUser: RequestUserInfo?,
        @PathVariable resultId: Long
    ): TarotResultResponseDto {
        return tarotService.getTarotResult(requestUser?.userKey, resultId)
    }

    @Operation(summary = "추천 타로 질문들 가져오기(4개)", description = "AI가 추천하는 타로 질문들을 가져옵니다.")
    @UserKeyIgnored
    @GetMapping("/question/recommends")
    fun getRecommendTarotQuestions(): RecommendTarotQuestionListResponseDto {
        return tarotService.getRecommendTarotQuestions()
    }

    @Operation(summary = "타로 히스토리 리스트", description = "본인이 받은 타로 히스토리 리스트를 가져옵니다.")
    @GetMapping("/history")
    fun getTarotHistory(
        @RequestUser requestUser: RequestUserInfo,
    ): TarotHistoryListResponseDto {
        return tarotHistoryService.getTarotHistoryList(requestUser.userKey)
    }
}