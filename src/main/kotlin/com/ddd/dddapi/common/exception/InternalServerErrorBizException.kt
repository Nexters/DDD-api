package com.ddd.dddapi.common.exception

class InternalServerErrorBizException(log: String): BizException(ErrorCode.INTERNAL_SERVER_ERROR, log)