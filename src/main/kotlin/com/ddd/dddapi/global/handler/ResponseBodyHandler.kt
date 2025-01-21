package com.ddd.dddapi.global.handler

import com.ddd.dddapi.common.dto.GlobalResponse
import com.ddd.dddapi.common.extension.getRequestId
import com.ddd.dddapi.common.extension.getRequestTime
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice


/**
 * 예외 응답, 정상 응답 모두 처리됨
 */
@RestControllerAdvice(basePackages = ["com.ddd.dddapi"])
class ResponseBodyHandler: ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val servletResponse = (response as ServletServerHttpResponse).servletResponse
        val status = servletResponse.status
        val resolve = HttpStatus.resolve(status) ?: return body

        return GlobalResponse(
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            success = resolve.is2xxSuccessful,
            data = body
        )
    }
}