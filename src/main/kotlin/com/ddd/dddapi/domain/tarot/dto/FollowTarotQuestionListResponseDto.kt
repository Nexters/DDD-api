package com.ddd.dddapi.domain.tarot.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FollowTarotQuestionListResponseDto(
    @field:Schema(description = "추천 꼬리질문 리스트", example = "['내일의 운세는?','올해 운세는?' ]")
    val questions: List<String>
)
