package com.ddd.dddapi.domain.user.repository

import com.ddd.dddapi.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByTempUserKey(tempUserKey: String): UserEntity?
}