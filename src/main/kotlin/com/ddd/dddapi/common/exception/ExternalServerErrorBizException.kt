package com.ddd.dddapi.common.exception

class ExternalServerErrorBizException(log: String): BizException(ErrorCode.EXTERNAL_SERVER_ERROR, log)