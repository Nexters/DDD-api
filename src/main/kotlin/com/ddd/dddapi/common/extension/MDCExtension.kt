package com.ddd.dddapi.common.extension

import com.ddd.dddapi.common.dto.RequestMetadata
import org.slf4j.MDC
import java.util.*

enum class LOG_TYPE {
    REQUEST_ID,
    REQUEST_URI,
    REQUEST_TIME,
}

fun setRequestMetaData(uri: String) {
    MDC.put(LOG_TYPE.REQUEST_ID.name, UUID.randomUUID().toString())
    MDC.put(LOG_TYPE.REQUEST_TIME.name, formattedNowLocalDateTime())
    MDC.put(LOG_TYPE.REQUEST_URI.name, uri)
}

fun getRequestMetaData(): RequestMetadata {
    return RequestMetadata(
        requestId = getRequestId(),
        requestUri = getRequestUri(),
        requestTime = getRequestTime()
    )
}

fun getRequestId(): String = MDC.get(LOG_TYPE.REQUEST_ID.name) ?: "알수없음"

fun getRequestUri(): String = MDC.get(LOG_TYPE.REQUEST_URI.name) ?: "알수없음"

fun getRequestTime(): String = MDC.get(LOG_TYPE.REQUEST_TIME.name) ?: "알수없음"

