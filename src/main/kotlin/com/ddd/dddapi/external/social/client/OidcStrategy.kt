package com.ddd.dddapi.external.social.client

import com.ddd.dddapi.external.social.dto.SocialUserInfo

interface OidcStrategy {
    fun authenticate(token: String): SocialUserInfo
}