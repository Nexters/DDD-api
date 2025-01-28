package com.ddd.dddapi.common.exception

class ForbiddenBizException(log: String): BizException(ErrorCode.FORBIDDEN, log)