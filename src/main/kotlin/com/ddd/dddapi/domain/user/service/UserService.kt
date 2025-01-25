package com.ddd.dddapi.domain.user.service

import com.ddd.dddapi.domain.user.entity.UserEntity

interface UserService {
    fun getOrCreateUser(tempUserKey: String): UserEntity
    fun getUserOrThrow(tempUserKey: String): UserEntity
}