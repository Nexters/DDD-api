package com.ddd.dddapi.external.notification.dto

enum class BizNotificationType(
    val title: String,
    val color: String
) {
    INFO("🔍 Info", "15871"),
    WARNING("⚠️ Warning", "16761344"),
    ERROR("🔥 Error", "16711680"),
    INVALID_QUESTION("❗️Invalid Question", "15871"),
}