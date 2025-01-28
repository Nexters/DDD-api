package com.ddd.dddapi.common.exception

class UnauthorizedBizException(log: String): BizException(ErrorCode.UNAUTHORIZED, log)
