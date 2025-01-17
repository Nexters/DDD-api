package com.ddd.dddapi.common.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 전역 포맷 설정
private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

fun formattedNowLocalDateTime(): String = LocalDateTime.now().format(dateFormatter)