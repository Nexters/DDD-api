package com.ddd.dddapi.domain.auth.controller

import com.ddd.dddapi.common.annotation.UserKeyIgnored
import com.ddd.dddapi.domain.auth.dto.AppleSignInRequestDto
import com.ddd.dddapi.domain.auth.dto.KakaoSignInRequestDto
import com.ddd.dddapi.domain.auth.dto.TokenResponseDto
import com.ddd.dddapi.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/auth")
@Tag(name = "00. Auth API")
class AuthController(
    private val authService: AuthService
) {
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 합니다")
    @UserKeyIgnored
    @PostMapping("/kakao")
    fun kakaoSignIn(
        @Valid @RequestBody request: KakaoSignInRequestDto
    ): TokenResponseDto {
        return authService.kakaoSignIn(request.authorizationCode)
    }

    @Operation(summary = "애플 로그인", description = "애플 로그인을 합니다")
    @UserKeyIgnored
    @PostMapping("/apple")
    fun appleSignIn(
        @Valid @RequestBody request: AppleSignInRequestDto
    ): TokenResponseDto {
        return authService.appleSignIn(request.idToken)
    }
}