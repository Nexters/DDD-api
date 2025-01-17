package com.ddd.dddapi.external.notification.client

import com.ddd.dddapi.external.notification.dto.*
import com.ddd.dddapi.external.notification.properties.BizNotificationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.format.DateTimeFormatter


@Component
@Async("bizNotificationExecutor")
class DiscordClient(
    val notificationProperties: BizNotificationProperties
): BizNotificationClient {

    private val restClient = RestClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    override fun sendError(message: DefaultNotificationMessage) {
        restClient.post()
            .uri(notificationProperties.errorMessage)
            .body(createDiscordRequest(BizNotificationType.ERROR, message))
            .retrieve()
            .body(Any::class.java)
    }

    override fun sendInvalidQuestion(message: DefaultNotificationMessage) {
        restClient.post()
            .uri(notificationProperties.invalidQuestion)
            .body(createDiscordRequest(BizNotificationType.INVALID_QUESTION, message))
            .retrieve()
            .body(Any::class.java)
    }

    private fun createDiscordRequest(type: BizNotificationType, message: DefaultNotificationMessage): DiscordMessage {
        val requestTime = message.requestTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return DiscordMessage(
            embeds = listOf(
                DiscordEmbeddedMessage(
                    title = type.title,
                    fields = listOf(
                        DiscordEmbeddedField(name = "Request Id", value = "◾️ ${message.requestId}"),
                        DiscordEmbeddedField(name = "Time", value = "◾️ $requestTime"),
                        DiscordEmbeddedField(name = "Request URI", value = "◾️ ${message.requestUri}"),
                        DiscordEmbeddedField(name = "Message", value = "◾️ ${message.message}",)
                    )
                )
            )
        )
    }
}