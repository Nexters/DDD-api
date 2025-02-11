package com.ddd.dddapi.domain.auth.service

import com.ddd.dddapi.common.dto.JwtUserInfo
import com.ddd.dddapi.common.util.JwtUtil
import com.ddd.dddapi.domain.auth.dto.TokenResponseDto
import com.ddd.dddapi.domain.user.entity.UserEntity
import com.ddd.dddapi.domain.user.repository.UserRepository
import com.ddd.dddapi.domain.user.service.helper.UserHelperService
import com.ddd.dddapi.external.social.client.OidcStrategy
import com.ddd.dddapi.external.social.dto.SocialUserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val kakaoClient: OidcStrategy,
    private val appleClient: OidcStrategy,
    private val jwtUtil: JwtUtil,
    private val userHelperService: UserHelperService,
    private val userRepository: UserRepository,
): AuthService {
    @Transactional
    override fun kakaoSignIn(authorizationCode: String): TokenResponseDto {
        val socialUserInfo = kakaoClient.authenticate(authorizationCode)
        val user = getOrCreateUser(socialUserInfo)
        val accessToken = jwtUtil.generateServiceToken(JwtUserInfo(userKey = user.userKey))

        return TokenResponseDto(accessToken)
    }

    @Transactional
    override fun appleSignIn(idToken: String): TokenResponseDto {
        val socialUserInfo = appleClient.authenticate(idToken)
        val user = getOrCreateUser(socialUserInfo)
        val accessToken = jwtUtil.generateServiceToken(JwtUserInfo(userKey = user.userKey))

        return TokenResponseDto(accessToken)
    }

    private fun getOrCreateUser(socialUserInfo: SocialUserInfo): UserEntity {
        return userHelperService.getUserOrNull(socialUserInfo.socialType, socialUserInfo.socialId)
            ?: userRepository.save(UserEntity(
                socialId = socialUserInfo.socialId,
                loginType = socialUserInfo.socialType,
            ))
    }
}