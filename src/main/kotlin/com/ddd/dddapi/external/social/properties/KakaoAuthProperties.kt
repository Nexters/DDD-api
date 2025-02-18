package com.ddd.dddapi.external.social.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "external.kakao-auth")
data class KakaoAuthProperties(
    val domain: String,
    val publicKeyPath: String,
    val receiveTokenPath: String,
    val redirectUri: String,
    val clientId: String,
)
