package com.ddd.dddapi.external.ai.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "external.ai-server")
data class AiServerProperties @ConstructorBinding constructor(
    val domain: String,
    val basePath: String,
    val classifyChatPath: String,
    val generalChatPath: String,
    val inappropriateChatPath: String,
    val tarotResultPath: String
)