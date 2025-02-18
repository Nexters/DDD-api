package com.ddd.dddapi.domain.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TokenResponseDto(
    @field:Schema(description = "타로냥 접근용 jwt Token", example = "jwt 형식")
    val accessToken: String,
)
