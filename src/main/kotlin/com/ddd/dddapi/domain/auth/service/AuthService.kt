package com.ddd.dddapi.domain.auth.service

import com.ddd.dddapi.domain.auth.dto.TokenResponseDto

interface AuthService {
    fun kakaoSignIn(authorizationCode: String): TokenResponseDto
    fun appleSignIn(idToken: String): TokenResponseDto
}