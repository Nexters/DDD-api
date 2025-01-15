package com.ddd.dddapi.global.controller

import com.ddd.dddapi.common.dto.DefaultResponseDto
import com.ddd.dddapi.common.exception.BizException
import com.ddd.dddapi.external.notification.client.BizNotificationClient
import com.ddd.dddapi.external.notification.dto.DefaultNotificationMessage
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
import java.time.LocalDateTime

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
        bizNotificationClient.sendError(
            createNotificationMessage(errorMessage, request.getDescription(false).replace("uri=", ""))
        )
        return ResponseEntity
            .status(400)
            .body(DefaultResponseDto(errorMessage))
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
            .body(DefaultResponseDto(errorMessage))
    }

    /**
     * 기타 Spring 내부 예외
     */
    override fun handleExceptionInternal(
        ex: Exception,
        @Nullable body: Any?,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.message ?: "잘못된 요청입니다."
        return ResponseEntity
            .status(400)
            .body(DefaultResponseDto(errorMessage))
    }

    /**
     * Biz 예외
     */
    @ExceptionHandler(BizException::class)
    fun handleBizException(e: BizException): ResponseEntity<DefaultResponseDto> {
        return ResponseEntity
            .status(e.errorCode.code)
            .body(DefaultResponseDto(e.log))
    }

    /**
     * 그 외 잡지 못한 예외
     */
    @ExceptionHandler(Exception::class)
    fun handleUncaughtException(e: Exception, request: HttpServletRequest): ResponseEntity<DefaultResponseDto> {
        val errorMessage = """
            [에러]
            message: ${e.message ?: "메세지 확인 불가. 체크 필요"}
            type: ${e.javaClass.simpleName}
        """.trimIndent()

        logger.error(errorMessage)
        bizNotificationClient.sendError(
            createNotificationMessage(errorMessage, request.requestURI ?: "URI 확인 불가. 체크 필요")
        )
        return ResponseEntity
            .status(500)
            .body(DefaultResponseDto("서버 에러"))
    }


    private fun createNotificationMessage(message: String, requestURI: String): DefaultNotificationMessage {
        return DefaultNotificationMessage(
            message = message,
            requestId = "TBD",
            requestTime = LocalDateTime.now(),
            requestUri = requestURI
        )
    }

}