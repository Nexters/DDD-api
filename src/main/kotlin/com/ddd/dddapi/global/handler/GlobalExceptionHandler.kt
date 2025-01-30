package com.ddd.dddapi.global.handler

import com.ddd.dddapi.common.dto.DefaultResponse
import com.ddd.dddapi.common.exception.BizException
import com.ddd.dddapi.common.extension.getRequestId
import com.ddd.dddapi.common.extension.getRequestTime
import com.ddd.dddapi.external.notification.client.BizNotificationClient
import com.ddd.dddapi.external.notification.dto.DefaultNotificationMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.lang.Nullable
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice(basePackages = ["com.ddd.dddapi"])
class GlobalExceptionHandler(
    val bizNotificationClient: BizNotificationClient
): ResponseEntityExceptionHandler() {
    /**
     * Validation 예외
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.bindingResult
            .fieldErrors.joinToString("\n") {
                "${it.field} 필드 : ${it.defaultMessage}"
            }
        return ResponseEntity
            .status(400)
            .body(DefaultResponse(errorMessage))
    }

    /**
     * JSON 파싱 예외(ex. enum 값이 잘못 들어온 경우)
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.message ?: "잘못된 요청입니다."
        return ResponseEntity
            .status(400)
            .body(DefaultResponse(errorMessage))
    }

    /**
     * 기타 Spring이 내부적으로 지원하는 예외
     * - 데이터 액세스, 빈 등과 관련된 예외
     */
    override fun handleExceptionInternal(
        ex: Exception,
        @Nullable body: Any?,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.message ?: "잘못된 요청입니다."
        bizNotificationClient.sendError(
            createNotificationMessage(errorMessage, getURI(request))
        )
        return ResponseEntity
            .status(500)
            .body(DefaultResponse(errorMessage))
    }

    /**
     * Biz 예외
     */
    @ExceptionHandler(BizException::class)
    fun handleBizException(e: BizException, request: HttpServletRequest): ResponseEntity<DefaultResponse> {
        val errorMessage = getErrorMessage(e, e.log)
        handleErrorExtras(errorMessage, request)
        return ResponseEntity
            .status(e.errorCode.code)
            .body(DefaultResponse(e.errorCode.message))
    }

    /**
     * 그 외 잡지 못한 예외
     */
    @ExceptionHandler(Exception::class)
    fun handleUncaughtException(e: Exception, request: HttpServletRequest): ResponseEntity<DefaultResponse> {
        val errorMessage = getErrorMessage(e, e.message ?: "알 수 없는 에러입니다.")
        handleErrorExtras(errorMessage, request)
        return ResponseEntity
            .status(500)
            .body(DefaultResponse(errorMessage))
    }

    private fun handleErrorExtras(message: String, request: HttpServletRequest) {
        logger.error{ message }
        bizNotificationClient.sendError(
            createNotificationMessage(message, request.requestURI ?: "URI 확인 불가. 체크 필요")
        )
    }

    private fun getErrorMessage(e: Exception, log: String): String {
        return """
            [에러]
            Error Class: ${e.javaClass.simpleName}
            $log
        """.trimIndent()
    }

    private fun getURI(request: WebRequest): String {
        return request.getDescription(false).replace("uri=", "")
    }

    private fun createNotificationMessage(message: String, requestURI: String): DefaultNotificationMessage {
        return DefaultNotificationMessage(
            message = message,
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            requestUri = requestURI
        )
    }
}