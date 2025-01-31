package com.ddd.dddapi.global.filter

import com.ddd.dddapi.common.dto.DefaultResponse
import com.ddd.dddapi.common.dto.GlobalResponse
import com.ddd.dddapi.common.exception.BizException
import com.ddd.dddapi.common.extension.alertMessage
import com.ddd.dddapi.common.extension.getRequestId
import com.ddd.dddapi.common.extension.getRequestTime
import com.ddd.dddapi.common.extension.getRequestUri
import com.ddd.dddapi.external.notification.client.BizNotificationClient
import com.ddd.dddapi.external.notification.dto.BizNotificationType
import com.ddd.dddapi.external.notification.dto.DefaultNotificationMessage
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class ExceptionHandleFilter(
    private val objectMapper: ObjectMapper,
    private val bizNotificationClient: BizNotificationClient
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BizException) {
            val message = e.alertMessage(e.errorCode.code, e.log)
            val globalResponse = createGlobalResponse(e.message)

            logger.warn(message)
            bizNotificationClient.sendUsual(
                createNotificationMessage(message),
                BizNotificationType.INFO
            )

            response.status = e.errorCode.code
            response.writer.write(objectMapper.writeValueAsString(globalResponse))
        } catch (e: Exception) {
            val errorMessage = e.alertMessage(500)
            val globalResponse = createGlobalResponse(e.message)

            logger.error(errorMessage)
            bizNotificationClient.sendError(
                createNotificationMessage(errorMessage)
            )

            response.status = 500
            response.writer.write(objectMapper.writeValueAsString(globalResponse))
        }
    }

    private fun createGlobalResponse(message: String?): GlobalResponse {
        return GlobalResponse(
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            success = false,
            data = DefaultResponse(message ?: "메세지를 알 수 없는 에러가 발생했습니다.")
        )
    }

    private fun createNotificationMessage(message: String): DefaultNotificationMessage {
        return DefaultNotificationMessage(
            message = message,
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            requestUri = getRequestUri(),
        )
    }
}