package com.ddd.dddapi.domain.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class KakaoSignInRequestDto(
    @field:Schema(description = "카카오 로그인 시 발급받은 authorization code", example = "asdf31dsf")
    val authorizationCode: String
)
