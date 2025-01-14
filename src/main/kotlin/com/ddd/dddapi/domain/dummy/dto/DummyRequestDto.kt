package com.ddd.dddapi.domain.dummy.dto

import com.ddd.dddapi.common.annotation.ValidEnum
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
data class DummyRequestDto(
    @field:NotBlank(message = "빈 값은 허용되지 않습니다.")
    @field:Size(min = 2, max = 20, message = "1자 이상 100자 이하로 입력해주세요.")
    @field:Parameter(description = "백엔드에게 하고 싶은 말", example = "화이팅")
    val say: String,

    @field:ValidEnum(enumClass = DummyRequestType::class)
    @field:Parameter(description = "Type", example = "THANKS")
    val type: DummyRequestType,
)

