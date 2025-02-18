package com.ddd.dddapi.common.dto

import com.ddd.dddapi.common.enums.ServiceRole

data class JwtUserInfo(
    val userKey: String,
    val role: ServiceRole = ServiceRole.USER,
)