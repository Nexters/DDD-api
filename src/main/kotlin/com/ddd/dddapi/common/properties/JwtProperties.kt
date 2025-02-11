package com.ddd.dddapi.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secretKey: String,
    val accessExpirationHours: Long,
    val refreshExpirationHours: Long,
    val issuer: String
)
