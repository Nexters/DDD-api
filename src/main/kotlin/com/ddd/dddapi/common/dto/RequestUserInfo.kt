package com.ddd.dddapi.common.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(hidden = true)
data class RequestUserInfo(
    @Schema(hidden = true)
    val userKey: String
)
