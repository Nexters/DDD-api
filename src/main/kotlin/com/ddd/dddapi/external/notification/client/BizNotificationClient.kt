package com.ddd.dddapi.external.notification.client

import com.ddd.dddapi.external.notification.dto.BizNotificationType
import com.ddd.dddapi.external.notification.dto.DefaultNotificationMessage
import org.springframework.stereotype.Component


interface BizNotificationClient {
    fun sendUsual(message: DefaultNotificationMessage, type: BizNotificationType)

    fun sendError(message: DefaultNotificationMessage)

    fun sendInvalidQuestion(message: DefaultNotificationMessage)
}