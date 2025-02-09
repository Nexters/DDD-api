package com.ddd.dddapi.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*

@Component
class JwtUtil(
    val objectMapper: ObjectMapper
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
}