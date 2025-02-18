package com.ddd.dddapi.common.exception

open class BizException(
    val errorCode: ErrorCode,
    val log: String
) : RuntimeException() {
    override val message: String?
        get() = errorCode.message
}
