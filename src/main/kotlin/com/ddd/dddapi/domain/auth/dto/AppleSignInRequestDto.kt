package com.ddd.dddapi.domain.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AppleSignInRequestDto(
    @field:Schema(description = "애플 로그인 시 발급받은 id token", example = "jwt 형식")
    val idToken: String,
)
