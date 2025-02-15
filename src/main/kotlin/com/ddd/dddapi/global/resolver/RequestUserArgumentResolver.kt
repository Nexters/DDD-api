package com.ddd.dddapi.global.resolver

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.dto.RequestUserInfo
import com.ddd.dddapi.common.enums.ServiceRole
import com.ddd.dddapi.common.exception.UnauthorizedBizException
import com.ddd.dddapi.common.util.JwtUtil
import com.fasterxml.jackson.module.kotlin.isKotlinClass
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties


class RequestUserArgumentResolver(
    private val jwtUtil: JwtUtil
): HandlerMethodArgumentResolver {
    private val guestUserHeader = "X-Guest-ID"
    private val authorizationHeader = "Authorization"

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(RequestUser::class.java) && parameter.parameterType == RequestUserInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        webRequest.getHeader(guestUserHeader)
            ?.let {
                return RequestUserInfo(it, role = ServiceRole.GUEST)
            }

        parameter.parameterType.kotlin.declaredMemberProperties
            .forEach {
                println(it.name)
                println(it.returnType.isMarkedNullable)
            }

        webRequest.getHeader(authorizationHeader)
            ?.let {
                val serviceToken = jwtUtil.validateServiceToken(it)
                return RequestUserInfo(userKey = serviceToken.userKey, role = serviceToken.role)
            }

        val isNullable = parameter.nestedParameterType.kotlin
            .createType().isMarkedNullable
        if (isNullable)
            return null
        else
            throw UnauthorizedBizException("헤더에 유저 식별값이 없습니다.")
    }
}