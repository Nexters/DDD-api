package com.ddd.dddapi.external.social.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "external.apple")
data class AppleProperties(
    val domain: String,
    val publicKeyPath: String,
)
