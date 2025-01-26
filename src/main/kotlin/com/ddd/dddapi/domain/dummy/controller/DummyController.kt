package com.ddd.dddapi.domain.dummy.controller

import com.ddd.dddapi.domain.dummy.dto.DummyRequestBodyDto
import com.ddd.dddapi.domain.dummy.dto.DummyRequestDto
import com.ddd.dddapi.domain.dummy.dto.DummyResponseDto
import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.annotation.UserKeyIgnored
import com.ddd.dddapi.common.dto.RequestUserInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Deprecated(message = "테스트용 컨트롤러입니다.")
@RestController
@RequestMapping("/dummy")
@Tag(name = "99. Dummy Test")
class DummyController {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Operation(summary = "테스트용 GET API 입니다.", description = "열심히 만들어보아요")
    @UserKeyIgnored
    @GetMapping("/health-check")
    fun healthCheck(@Valid @ModelAttribute request: DummyRequestDto): DummyResponseDto {
        return DummyResponseDto("건강합니다")
    }

    @Operation(summary = "테스트용 GET API 입니다.", description = "유저 인증 쿠키가 필요합니다.")
    @GetMapping("/health-check22")
    fun userKeyCheck(
        @RequestUser requestUser: RequestUserInfo,
        @Valid request: DummyRequestDto
    ): DummyResponseDto {
        return DummyResponseDto("User Key 확인완료 : ${requestUser.userKey}")
    }

    @Operation(summary = "테스트용 POST API 입니다.", description = "유저 인증 쿠키가 필요합니다.")
    @PostMapping("/health-check333")
    fun userKeyCheck22(
        @RequestUser requestUser: RequestUserInfo,
        @Valid @RequestBody request: DummyRequestBodyDto
    ): DummyResponseDto {
        return DummyResponseDto("User Key 확인완료 : ${requestUser.userKey}")
    }
}
