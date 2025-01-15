package com.ddd.dddapi.external.notification.dto

import java.time.LocalDateTime

data class DefaultNotificationMessage(
    val requestId: String,
    val requestTime: LocalDateTime,
    val requestUri: String,
    val message: String,
)
