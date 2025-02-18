package com.ddd.dddapi.external.social.dto

import com.ddd.dddapi.common.enums.LoginType

data class SocialUserInfo(
    val socialType: LoginType,
    val socialId: String,
    val email: String? = null,
    val name: String? = null,
    val profileImage: String? = null,
)
