package com.ddd.dddapi.domain.user.service.helper

import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.domain.common.annotation.HelperService
import com.ddd.dddapi.domain.user.entity.UserEntity
import com.ddd.dddapi.domain.user.repository.UserRepository
import org.springframework.transaction.annotation.Transactional

@HelperService
@Transactional(readOnly = true)
class UserHelperService(
    private val userRepository: UserRepository
) {
    fun getUserOrThrow(tempUserKey: String): UserEntity {
        return userRepository.findByTempUserKey(tempUserKey)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [tempUserKey: $tempUserKey]")
    }
}