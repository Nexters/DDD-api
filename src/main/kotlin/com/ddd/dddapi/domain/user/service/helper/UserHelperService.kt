package com.ddd.dddapi.domain.user.service.helper

import com.ddd.dddapi.common.enums.LoginType
import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.domain.common.annotation.HelperService
import com.ddd.dddapi.domain.user.entity.UserEntity
import com.ddd.dddapi.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@HelperService
@Transactional(readOnly = true)
class UserHelperService(
    private val userRepository: UserRepository
) {
    fun getUserOrThrow(id: Long): UserEntity {
        return userRepository.findByIdOrNull(id)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [id: $id]")
    }

    fun getUserOrThrow(tempUserKey: String): UserEntity {
        return userRepository.findByUserKey(tempUserKey)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [tempUserKey: $tempUserKey]")
    }

    fun getUserOrThrow(type: LoginType, key: String): UserEntity {
        if (type == LoginType.GUEST) return getUserOrThrow(key)
        return userRepository.findByLoginTypeAndSocialId(type, key)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [type: $type socialId: $key]")
    }

    fun getUserOrNull(type: LoginType, key: String): UserEntity? {
        if (type == LoginType.GUEST) return userRepository.findByUserKey(key)
        return userRepository.findByLoginTypeAndSocialId(type, key)
    }
}