package com.ddd.dddapi.common.annotation

import io.swagger.v3.oas.annotations.media.Schema


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Schema(hidden = true)
annotation class RequestUser(
    @Schema(hidden = true)
    val value: String = "userKey"
)
