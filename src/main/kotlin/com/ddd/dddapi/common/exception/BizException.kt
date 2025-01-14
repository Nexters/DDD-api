package com.ddd.dddapi.common.exception

open class BizException(
    val errorCode: ErrorCode,
    val log: String
) : RuntimeException()
