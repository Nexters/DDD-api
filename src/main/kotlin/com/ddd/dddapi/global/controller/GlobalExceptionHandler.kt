package com.ddd.dddapi.global.controller

import com.ddd.dddapi.common.dto.DefaultResponseDto
import com.ddd.dddapi.common.exception.BizException
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
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {
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
    fun handleUncaughtException(e: Exception): ResponseEntity<DefaultResponseDto> {
        logger.error("""
            [에러]
            message: ${e.message}
            type: ${e.javaClass.simpleName}
        """.trimIndent())
        // TODO: 디스코드 알림 연동
        return ResponseEntity
            .status(500)
            .body(DefaultResponseDto("서버 에러"))
    }

}