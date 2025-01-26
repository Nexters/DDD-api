package com.ddd.dddapi.domain.user.service

import com.ddd.dddapi.common.exception.BadRequestBizException
import com.ddd.dddapi.domain.user.entity.UserEntity
import com.ddd.dddapi.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
): UserService {
    @Transactional
    override fun getOrCreateUser(tempUserKey: String): UserEntity {
        val user = userRepository.findByTempUserKey(tempUserKey)
        return user ?: userRepository.save(UserEntity(tempUserKey = tempUserKey))
    }

    @Transactional(readOnly = true)
    override fun getUserOrThrow(tempUserKey: String): UserEntity {
        // TODO: 예외 구체화
        userRepository.findByTempUserKey(tempUserKey)?.let {
            return it
        } ?: throw BadRequestBizException("유저가 존재하지 않습니다")
    }
}