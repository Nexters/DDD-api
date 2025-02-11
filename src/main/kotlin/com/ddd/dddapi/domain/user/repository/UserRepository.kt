package com.ddd.dddapi.domain.user.repository

import com.ddd.dddapi.common.enums.LoginType
import com.ddd.dddapi.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByUserKey(userKey: String): UserEntity?

    fun findByLoginTypeAndSocialId(loginType: LoginType, socialId: String): UserEntity?
}