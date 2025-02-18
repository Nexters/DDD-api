package com.ddd.dddapi.common.dto

import com.ddd.dddapi.common.enums.ServiceRole
import io.swagger.v3.oas.annotations.media.Schema

@Schema(hidden = true)
data class RequestUserInfo(
    @Schema(hidden = true)
    val userKey: String,
    @Schema(hidden = true)
    val role: ServiceRole = ServiceRole.GUEST,
)
