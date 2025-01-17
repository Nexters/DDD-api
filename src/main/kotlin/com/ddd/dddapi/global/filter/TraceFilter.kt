package com.ddd.dddapi.global.filter

import com.ddd.dddapi.common.extension.setRequestMetaData
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter

class TraceFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        setRequestMetaData(request.requestURI)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}