package com.ddd.dddapi.external.notification.dto

enum class BizNotificationType(
    val title: String,
) {
    ERROR("🔥 Error"),
    INVALID_QUESTION("❗️Invalid Question"),
}