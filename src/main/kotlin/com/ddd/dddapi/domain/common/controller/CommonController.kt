package com.ddd.dddapi.domain.common.controller

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.dto.DefaultResponse
import com.ddd.dddapi.common.dto.RequestUserInfo
import com.ddd.dddapi.domain.common.dto.ShareLoggingRequestDto
import com.ddd.dddapi.domain.common.service.CommonServiceImpl
import com.ddd.dddapi.domain.common.service.ShareLogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/common")
@Tag(name = "00. Common API")
class CommonController(
    private val commonService: ShareLogService
) {
    @Operation(summary = "질문 공유하기 로깅", description = "공유하기 시 로깅용 API")
    @PostMapping("/share")
    fun shareLog(
        @RequestUser requestUser: RequestUserInfo,
        @Valid @RequestBody request: ShareLoggingRequestDto
    ): DefaultResponse{
        commonService.saveShareLog(request.resultId)
        return DefaultResponse()
    }
}