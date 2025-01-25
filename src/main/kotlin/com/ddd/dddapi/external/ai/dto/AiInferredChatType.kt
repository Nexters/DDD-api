package com.ddd.dddapi.external.ai.dto

enum class AiInferredChatType(
    val description: String
) {
    INAPPROPRIATE("부적절한 질문"),
    GENERAL("일반 질문"),
    TAROT("타로 질문"),
}