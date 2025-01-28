package com.ddd.dddapi.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String
) {
    UNAUTHORIZED(401, "인증되지 않은 사용자입니다"),
    FORBIDDEN(403, "권한이 없는 사용자입니다"),
    BAD_REQUEST(404, "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다"),
    EXTERNAL_SERVER_ERROR(500, "외부 서버 에러입니다"),
}