package com.ddd.dddapi.domain.tarot.service

import com.ddd.dddapi.domain.chat.dto.ChatMessageResponseDto
import com.ddd.dddapi.domain.tarot.dto.RecommendTarotQuestionListResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotResultResponseDto
import com.ddd.dddapi.domain.tarot.dto.TarotSelectRequestDto

interface TarotService {
    fun selectTarot(tempUserKey: String, request: TarotSelectRequestDto): ChatMessageResponseDto
    fun getTarotResult(userKey: String?, tarotResultId: Long): TarotResultResponseDto
    fun getRecommendTarotQuestions(): RecommendTarotQuestionListResponseDto
}