package com.ddd.dddapi.common.exception

class BadRequestBizException(log: String): BizException(ErrorCode.BAD_REQUEST, log)
