package com.ddd.dddapi.common.enums

import com.ddd.dddapi.external.ai.dto.AiInferredChatType

enum class MessageType {
    USER_NORMAL,
    USER_INVALID_QUESTION,
    USER_TAROT_QUESTION,
    USER_TAROT_QUESTION_ACCEPTANCE,
    USER_TAROT_QUESTION_DECLINE,
    USER_FOLLOW_QUESTION,

    SYSTEM_HELLO,
    SYSTEM_NORMAL_REPLY,
    SYSTEM_INVALID_QUESTION_REPLY,
    SYSTEM_TAROT_QUESTION_REPLY,
    SYSTEM_TAROT_QUESTION_ACCEPTANCE_REPLY,
    SYSTEM_TAROT_RESULT;

    companion object {
        fun userMessageFrom(value: AiInferredChatType): MessageType =
            when(value) {
                AiInferredChatType.GENERAL -> USER_NORMAL
                AiInferredChatType.INAPPROPRIATE -> USER_INVALID_QUESTION
                AiInferredChatType.TAROT -> USER_TAROT_QUESTION
            }

        fun systemMessageFrom(value: AiInferredChatType): MessageType =
            when(value) {
                AiInferredChatType.GENERAL -> SYSTEM_NORMAL_REPLY
                AiInferredChatType.INAPPROPRIATE -> SYSTEM_INVALID_QUESTION_REPLY
                AiInferredChatType.TAROT -> SYSTEM_TAROT_QUESTION_REPLY
            }
    }

    fun replyType(): MessageType =
        when(this) {
            USER_INVALID_QUESTION -> SYSTEM_INVALID_QUESTION_REPLY
            USER_FOLLOW_QUESTION,
            USER_TAROT_QUESTION -> SYSTEM_TAROT_QUESTION_REPLY
            USER_NORMAL,
            USER_TAROT_QUESTION_DECLINE -> SYSTEM_NORMAL_REPLY
            USER_TAROT_QUESTION_ACCEPTANCE -> SYSTEM_TAROT_QUESTION_ACCEPTANCE_REPLY
            else -> SYSTEM_NORMAL_REPLY
        }
}