package com.ddd.dddapi.common.extension


fun Exception.alertMessage(status: Int, log: String = this.message ?: "에러메세지 확인 불가. 체크 필요"): String {
    return """
        [예외 ${status}]
        Error Class: ${this.javaClass.simpleName}
        $log
    """.trimIndent()
}