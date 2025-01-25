package com.ddd.dddapi.external.ai.client

import com.ddd.dddapi.external.ai.dto.*
import com.ddd.dddapi.external.ai.properties.AiServerProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class AiClientV1(
    private val aiServerProperties: AiServerProperties
): AiClient {
    private val restClient = RestClient.builder()
        .baseUrl(aiServerProperties.domain + aiServerProperties.basePath)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    override fun chatClassification(request: AiChatCommonRequestDto): AiChatClassifyResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, AiChatClassifyResponseDto>(
            aiServerProperties.classifyChatPath,
            request
        )
    }

    override fun chatCasually(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, AiChatCommonResponseDto>(
            aiServerProperties.generalChatPath,
            request
        )
    }

    override fun chatInappropriate(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, AiChatCommonResponseDto>(
            aiServerProperties.inappropriateChatPath,
            request
        )
    }

    override fun chatTarotResult(request: AiTarotResultRequestDto): AiTarotResultResponseDto {
        return requestPostToAiServer<AiTarotResultRequestDto, AiTarotResultResponseDto>(
            aiServerProperties.tarotResultPath,
            request
        )
    }

    private inline fun <reified Req : Any,reified Res : Any> requestPostToAiServer(path: String, request: Req): Res {
        val response = restClient.post()
            .uri(path)
            .body(request)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError) { req, res ->
                // TODO: 예외 디테일하게 선언하기
                throw RuntimeException("Failed to request to ai server")
            }
            .onStatus(HttpStatusCode::is5xxServerError) { req, res ->
                // TODO: 예외 디테일하게 선언하기
                throw RuntimeException("AI Server Error")
            }
            .toEntity(Res::class.java)

        // TODO: 예외 디테일하게 선언하기
        response.body?.let {
            return it
        } ?: throw RuntimeException("Failed to request to ai server")
    }
}