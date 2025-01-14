package com.ddd.dddapi.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String
) {
    BAD_REQUEST(400, "잘못된 요청입니다"),
    FORBIDDEN(403, "권한이 없습니다"),
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다"),
}