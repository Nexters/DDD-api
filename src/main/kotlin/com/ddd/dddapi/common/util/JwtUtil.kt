package com.ddd.dddapi.common.util

import com.ddd.dddapi.common.dto.JwtUserInfo
import com.ddd.dddapi.common.enums.ServiceRole
import com.ddd.dddapi.common.exception.UnauthorizedBizException
import com.ddd.dddapi.common.properties.JwtProperties
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.spec.SecretKeySpec

private const val USER_TOKEN_SUBJECT = "token"
private const val JWT_CLAIM_USER_KEY = "userKey"
private const val JWT_CLAIM_ROLE = "role"

@Component
class JwtUtil(
    val objectMapper: ObjectMapper,
    val jwtProperties: JwtProperties
) {
    fun convertToPublicKey(n: String, e: String): PublicKey {
        val modulus = BigInteger(1, Base64.getUrlDecoder().decode(n))
        val exponent = BigInteger(1, Base64.getUrlDecoder().decode(e))
        val spec = RSAPublicKeySpec(modulus, exponent)
        return KeyFactory.getInstance("RSA").generatePublic(spec)
    }

    fun claims(key: PublicKey, token: String): Claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body

    fun decodeJwtHeader(token: String): Map<String, Any> {
        val parts = token.split(".")
        val headerJson = String(Base64.getUrlDecoder().decode(parts[0]))
        return objectMapper.readValue(headerJson, Map::class.java) as Map<String, Any>
    }

    fun generateServiceToken(jwtUserInfo: JwtUserInfo): String {
        return Jwts.builder()
            .claim(JWT_CLAIM_USER_KEY, jwtUserInfo.userKey)
            .claim(JWT_CLAIM_ROLE, jwtUserInfo.role.name)
            .setSubject(USER_TOKEN_SUBJECT)
            .setIssuer(jwtProperties.issuer)
            .setExpiration(
                Date.from(
                    Instant.now().plus(jwtProperties.accessExpirationHours, ChronoUnit.HOURS)
                )
            ) // 100년
            .signWith(SecretKeySpec(jwtProperties.secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName))
            .compact()
    }

    fun validateServiceToken(token: String): JwtUserInfo = try {
        Jwts.parserBuilder()
            .setSigningKey(jwtProperties.secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .let {
                JwtUserInfo(
                    userKey = it.get(key = JWT_CLAIM_USER_KEY) as String,
                    role = enumValueOf(
                        it.get(key = JWT_CLAIM_ROLE) as? String ?: ServiceRole.GUEST.name),
                )
            }
    } catch (e: Exception) {
        throw UnauthorizedBizException("잘못된 서비스 토큰입니다.")
    }
}