package com.ddd.dddapi.domain.dummy.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "더미 응답 DTO")
data class DummyResponseDto(
    @field:Schema(description = "클라이언트에게 하고 싶은 말", example = "프론트 화이링~")
    val message: String
)