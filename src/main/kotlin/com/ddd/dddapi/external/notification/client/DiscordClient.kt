package com.ddd.dddapi.external.notification.client

import com.ddd.dddapi.external.notification.dto.*
import com.ddd.dddapi.external.notification.properties.BizNotificationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.web.client.RestClient

private const val NOTIFICATION_EXECUTOR_NAME = "bizNotificationExecutor"

@Component
class DiscordClient(
    val notificationProperties: BizNotificationProperties
): BizNotificationClient {
    private val restClient = RestClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    @Async(NOTIFICATION_EXECUTOR_NAME)
    override fun sendError(message: DefaultNotificationMessage) {
        restClient.post()
            .uri(notificationProperties.errorMessage)
            .body(createDiscordRequest(BizNotificationType.ERROR, message))
            .retrieve()
            .body(Any::class.java)
    }

    @Async(NOTIFICATION_EXECUTOR_NAME)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    override fun sendInvalidQuestion(message: DefaultNotificationMessage) {
        restClient.post()
            .uri(notificationProperties.invalidQuestion)
            .body(createDiscordRequest(BizNotificationType.INVALID_QUESTION, message))
            .retrieve()
            .body(Any::class.java)
    }

    private fun createDiscordRequest(type: BizNotificationType, message: DefaultNotificationMessage): DiscordMessage {
        return DiscordMessage(
            embeds = listOf(
                DiscordEmbeddedMessage(
                    title = type.title,
                    fields = listOf(
                        DiscordEmbeddedField(name = "Request Id", value = "◾️ ${message.requestId}"),
                        DiscordEmbeddedField(name = "Request Time", value = "◾️ ${message.requestTime}"),
                        DiscordEmbeddedField(name = "Request URI", value = "◾️ ${message.requestUri}"),
                        DiscordEmbeddedField(name = "Message", value = "◾️ ${message.message}",)
                    )
                )
            )
        )
    }
}