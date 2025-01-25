package com.ddd.dddapi.external.ai.client

import com.ddd.dddapi.external.ai.dto.*

interface AiClient {
    /**
     * 메세지의 유형을 분류
     */
    fun chatClassification(request: AiChatCommonRequestDto): AiChatClassifyResponseDto

    /**
     * 일반 대화
     */
    fun chatCasually(request: AiChatCommonRequestDto): AiChatCommonResponseDto

    /**
     * 부적절한 대화
     */
    fun chatInappropriate(request: AiChatCommonRequestDto): AiChatCommonResponseDto

    /**
     * 타로 결과
     */
    fun chatTarotResult(request: AiTarotResultRequestDto): AiTarotResultResponseDto
}