package com.ddd.dddapi.global.resolver

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.dto.RequestUserInfo
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


@Configuration
class RequestUserArgumentResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(RequestUser::class.java) && parameter.parameterType == RequestUserInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest
        val cookies = request.cookies

        val cookieValueAnnotation = parameter.getParameterAnnotation(RequestUser::class.java)
        val cookieKey = cookieValueAnnotation!!.value

        cookies?.firstOrNull { it.name.equals(cookieKey) }
            ?.let { cookie ->
                return RequestUserInfo(cookie.value)
            }

        // FIXME: 개발시에만 - 테스트 후 제거해야함
        return RequestUserInfo("admin")
    }
}