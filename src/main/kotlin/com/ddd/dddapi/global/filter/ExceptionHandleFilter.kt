package com.ddd.dddapi.global.filter

import com.ddd.dddapi.common.dto.DefaultResponse
import com.ddd.dddapi.common.dto.GlobalResponse
import com.ddd.dddapi.common.exception.BizException
import com.ddd.dddapi.common.extension.getRequestId
import com.ddd.dddapi.common.extension.getRequestTime
import com.ddd.dddapi.external.notification.client.BizNotificationClient
import com.ddd.dddapi.external.notification.dto.DefaultNotificationMessage
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter

class ExceptionHandleFilter(
    private val objectMapper: ObjectMapper,
    private val bizNotificationClient: BizNotificationClient
): OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BizException) {
            val globalResponse = createGlobalResponse(e.log)
            response.status = e.errorCode.code
            response.writer.write(objectMapper.writeValueAsString(globalResponse))
        } catch (e: Exception) {
            val errorMessage = """
                [에러]
                message: ${e.message ?: "메세지 확인 불가. 체크 필요"}
                type: ${e.javaClass.simpleName}
            """.trimIndent()
            val globalResponse = createGlobalResponse(errorMessage)

            log.error(errorMessage)
            bizNotificationClient.sendError(
                createNotificationMessage(errorMessage, request.requestURI ?: "URI 확인 불가. 체크 필요")
            )

            response.status = 500
            response.writer.write(objectMapper.writeValueAsString(globalResponse))
        }
    }

    private fun createGlobalResponse(message: String): GlobalResponse {
        return GlobalResponse(
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            success = false,
            data = DefaultResponse(message)
        )
    }

    private fun createNotificationMessage(message: String, requestURI: String): DefaultNotificationMessage {
        return DefaultNotificationMessage(
            message = message,
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            requestUri = requestURI,
        )
    }
}