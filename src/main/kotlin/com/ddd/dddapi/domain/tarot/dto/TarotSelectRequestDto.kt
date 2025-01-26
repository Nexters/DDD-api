package com.ddd.dddapi.domain.tarot.dto

import com.ddd.dddapi.common.enums.TarotInfo
import io.swagger.v3.oas.annotations.media.Schema

data class TarotSelectRequestDto(
    @field:Schema(description = "채팅방 ID", example = "12345")
    val roomId: Long,
    @field:Schema(description = "선택한 타로 카드이름", example = "S_01")
    val tarotName: TarotInfo,
)
