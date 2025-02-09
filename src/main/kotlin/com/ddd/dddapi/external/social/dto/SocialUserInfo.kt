package com.ddd.dddapi.external.social.dto

data class SocialUserInfo(
    val socialType: SocialType,
    val socialId: String,
    val email: String? = null,
    val name: String? = null,
    val profileImage: String? = null,
)
