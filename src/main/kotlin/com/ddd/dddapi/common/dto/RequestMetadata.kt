package com.ddd.dddapi.common.dto

data class RequestMetadata(
    val requestId: String,
    val requestUri: String,
    val requestTime: String,
)
