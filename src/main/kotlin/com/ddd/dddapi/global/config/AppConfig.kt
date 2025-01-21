package com.ddd.dddapi.global.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

@Configuration
class AppConfig(
    val objectMapper: ObjectMapper
) {

    @PostConstruct
    fun setupObjectMapper() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}