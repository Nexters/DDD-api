package com.ddd.dddapi.external.social.client

import com.ddd.dddapi.common.enums.LoginType
import com.ddd.dddapi.common.util.JwtUtil
import com.ddd.dddapi.external.social.dto.SocialUserInfo
import com.ddd.dddapi.external.social.properties.AppleProperties
import io.jsonwebtoken.Claims
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.security.PublicKey

@Component
class AppleClient(
    val properties: AppleProperties,
    val jwtUtil: JwtUtil
): OidcStrategy {
    private val restClient = RestClient.builder()
        .baseUrl(properties.domain)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    override fun authenticate(token: String): SocialUserInfo {
        val publicKey = fetchPublicKey(token)
        val claims = getClaims(publicKey, token)
        return createSocialUserInfo(claims)
    }

    private fun fetchPublicKey(idToken: String): PublicKey {
        val header = jwtUtil.decodeJwtHeader(idToken)
        val jwks = restClient.get()
            .uri(properties.publicKeyPath)
            .retrieve()
            .toEntity(Map::class.java) as Map<String, Any>

        val keys = jwks["keys"] as List<Map<String, Any>>
        val keyData = keys.find { it["kid"] == header["kid"] } ?: throw IllegalArgumentException("No matching key found")
        val n = keyData["n"] as String
        val e = keyData["e"] as String

        return jwtUtil.convertToPublicKey(n, e)
    }

    private fun getClaims(publicKey: PublicKey, idToken: String): Claims = jwtUtil.claims(publicKey, idToken)

    private fun createSocialUserInfo(claims: Claims): SocialUserInfo =
        SocialUserInfo(
            socialType = LoginType.APPLE,
            socialId = claims.subject as String,
        )
}