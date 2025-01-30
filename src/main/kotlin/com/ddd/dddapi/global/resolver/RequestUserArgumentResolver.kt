package com.ddd.dddapi.global.resolver

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.dto.RequestUserInfo
import com.ddd.dddapi.common.exception.UnauthorizedBizException
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.core.env.Environment
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


@Configuration
class RequestUserArgumentResolver: HandlerMethodArgumentResolver {
    private val tempUserHeaderName = "X-Guest-ID"

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(RequestUser::class.java) && parameter.parameterType == RequestUserInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        webRequest.getHeader(tempUserHeaderName)
            ?.let {
                return RequestUserInfo(it)
            }

        val servletRequest = (webRequest as? ServletWebRequest)?.request
            ?: throw UnauthorizedBizException("ServletRequest is not available")
        val headers = servletRequest.headerNames.toList().associateWith { servletRequest.getHeader(it) }
        val errorMessage = """
            [RequestUserInfo Resolver Error]
            Method: ${servletRequest.method}
            URI: ${servletRequest.requestURI}
            Query: ${servletRequest.queryString ?: "N/A"}
            Headers: $headers
        """.trimIndent()
        throw UnauthorizedBizException(errorMessage)
    }
}