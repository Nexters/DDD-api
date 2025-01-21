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

private const val USER_KEY_SCHEME: String = "간편 유저 확인 Key"
private const val USER_COOKIE_KEY_NAME: String = "userKey"

@Configuration
class OpenApiConfig {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(apiInfo())
        .components(
            Components().addSecuritySchemes(USER_KEY_SCHEME, userKeyScheme())
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
        .`in`(SecurityScheme.In.COOKIE)
        .name(USER_COOKIE_KEY_NAME)
        .description("""
            ❗️Swagger 보안이슈로 인해 쿠키를 담아 요청할 수 없습니다.(현재 서버 로직으로 admin 계정으로 처리중)
            실제 요청시 프론트 레벨에서 withCredential = true로 설정 & 쿠키 키 'userKey'에 담아 요청합니다.
        """.trimIndent())

    private fun securityRequirementList() = listOf(
        SecurityRequirement().addList(USER_KEY_SCHEME)
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