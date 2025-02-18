package com.ddd.dddapi.external.notification.dto


data class DiscordMessage(
    val content: String = "",
    val embeds: List<DiscordEmbeddedMessage>,
)

data class DiscordEmbeddedMessage(
    val title: String,
    val description: String = "",
    val color: String,
    val fields: List<DiscordEmbeddedField>
)

data class DiscordEmbeddedField(
    val name: String,
    val value: String,
    val text: String = ""
)