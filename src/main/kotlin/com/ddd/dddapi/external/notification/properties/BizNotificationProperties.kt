package com.ddd.dddapi.external.notification.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "biz.notification-channel")
data class BizNotificationProperties @ConstructorBinding constructor(
    val errorMessage: String,
    val invalidQuestion: String,
)
