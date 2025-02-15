package com.ddd.dddapi.external.ai.client

import com.ddd.dddapi.external.ai.dto.*
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class AiLocalClient: AiClient {
    override fun chatClassification(request: AiChatCommonRequestDto): AiChatClassifyResponseDto {
        return if (request.chat == "오늘의 운세")
            AiChatClassifyResponseDto(AiInferredChatType.TAROT, "타로 질문")
        else AiChatClassifyResponseDto(AiInferredChatType.GENERAL, "일반 대화")
    }

    override fun chatCasually(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return AiChatCommonResponseDto("일반 대화에 대한 응답입니다")
    }

    override fun chatInappropriate(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return AiChatCommonResponseDto("부적절한 대화에 대한 응답입니다")
    }

    override fun chatTarotQuestion(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return AiChatCommonResponseDto("타로를 한 번 뽑아볼까?(타로질문에 대한 응답)")
    }

    override fun chatTarotResult(request: AiTarotResultRequestDto): AiTarotResultResponseDto {
        return AiTarotResultResponseDto(
            type = "연애운",
            summaryOfDescriptionOfCard = "카드 설명 요약",
            descriptionOfCard = "카드 설명",
            summaryOfAnalysis = "질문 분석 요약",
            analysis = "질문 분석 설명",
            summaryOfAdvice = "조언 요약",
            advice = "조언 설명",
            comprehensiveSummary = "종합 요약"
        )
    }

    override fun tarotQuestionSummary(request: AiTarotQuestionSummaryRequestDto): AiTarotQuestionSummaryResponseDto {
        return AiTarotQuestionSummaryResponseDto("요약한 질문")
    }

    override fun tarotFollowQuestion(request: AiTarotFollowQuestionRequestDto): AiTarotFollowQuestionResponseDto {
        return AiTarotFollowQuestionResponseDto(listOf("타로 질문에 대한 후속 질문"))
    }
}