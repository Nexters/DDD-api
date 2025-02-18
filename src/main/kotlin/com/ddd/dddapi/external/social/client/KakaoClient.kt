package com.ddd.dddapi.external.social.client

import com.ddd.dddapi.common.enums.LoginType
import com.ddd.dddapi.common.util.JwtUtil
import com.ddd.dddapi.external.social.dto.SocialUserInfo
import com.ddd.dddapi.external.social.properties.KakaoAuthProperties
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Component
import java.security.PublicKey
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient


@Component
class KakaoClient(
    private val properties: KakaoAuthProperties,
    private val jwtUtil: JwtUtil
): OidcStrategy  {
    private val restClient = RestClient.builder()
        .baseUrl(properties.domain)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    override fun authenticate(token: String): SocialUserInfo {
        val idToken = preAuthenticate(token)
        val publicKey = fetchPublicKey()
        val claims = getClaims(publicKey, idToken)
        return createSocialUserInfo(claims)
    }

    private fun preAuthenticate(authorizationCode: String): String {
        val tokenResponse = restClient.get()
            .uri("${properties.receiveTokenPath}?grant_type=authorization_code&client_id=${properties.clientId}&redirect_uri=${properties.redirectUri}&code=${authorizationCode}")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .retrieve()
            .body(Map::class.java) as Map<String, Any>

        return tokenResponse["id_token"] as String
    }

    private fun fetchPublicKey(): PublicKey {
        val jwks = restClient.get()
            .uri(properties.publicKeyPath)
            .retrieve()
            .toEntity(Map::class.java) as Map<String, Any>
        val keys = jwks["keys"] as List<Map<String, Any>>
        val keyData = keys[0]
        val n = keyData["n"] as String
        val e = keyData["e"] as String

        return jwtUtil.convertToPublicKey(n, e)
    }

    private fun getClaims(publicKey: PublicKey, idToken: String): Claims = jwtUtil.claims(publicKey, idToken)

    private fun createSocialUserInfo(claims: Claims): SocialUserInfo =
        SocialUserInfo(
            socialType = LoginType.KAKAO,
            socialId = claims.subject as String,
        )
}