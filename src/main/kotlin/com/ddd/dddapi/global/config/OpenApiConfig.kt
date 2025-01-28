package com.ddd.dddapi.global.config

import com.ddd.dddapi.common.annotation.UserKeyIgnored
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.slf4j.LoggerFactory
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod


private const val USER_IDENTIFICATION_SCHEME: String = "유저 식별 Key"
private const val USER_IDENTIFICATION_HEADER_NAME: String = "X-Guest-ID"


@Configuration
class OpenApiConfig {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(apiInfo())
        .components(
            Components().addSecuritySchemes(USER_IDENTIFICATION_SCHEME, userKeyScheme())
        )
        .security(securityRequirementList())

    private fun apiInfo() = Info()
        .title("DDD API 명세")
        .description("""
            DDD(개발 한 스푼) 어플리케이션을 위한 API 명세서입니다.
            - 모든 API의 Path는 **'/api'로 시작**합니다.
            - 모든 API의 응답은 **공통 응답 형식**을 가집니다. data에 실제 응답 데이터가 들어갑니다.
                ```json
                {
                  "requestId": "서버생성 요청ID",
                  "requestTime": "요청시간",
                  "success": "성공여부",
                  "data": {
                    JSON 데이터
                  }
                }
                ```
        """.trimIndent())
        .version("v1.0.0")

    private fun userKeyScheme() = SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .`in`(SecurityScheme.In.HEADER)
        .name(USER_IDENTIFICATION_HEADER_NAME)
        .description("""
            유저 식별을 위한 Key입니다. **X-Guest-ID 헤더**에 생성한 UUID를 넣어주세요.
        """.trimIndent())

    private fun securityRequirementList() = listOf(
        SecurityRequirement().addList(USER_IDENTIFICATION_SCHEME)
    )

    @Bean
    fun customOperationCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            val hasUserKeyIgnored = handlerMethod.hasMethodAnnotation(UserKeyIgnored::class.java)
            if (hasUserKeyIgnored) operation.security = emptyList()

            operation
        }
    }
}