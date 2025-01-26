package com.ddd.dddapi.common.enums

import io.swagger.v3.oas.annotations.media.Schema

enum class MessageIntent {
    @Schema(description = "일반 메시지")
    NORMAL,
    @Schema(description = "타로 질문 승낙시")
    TAROT_ACCEPT,
    @Schema(description = "타로 질문 거절시")
    TAROT_DECLINE,
    @Schema(description = "추천 질문을 보낼때")
    RECOMMEND_QUESTION,
}