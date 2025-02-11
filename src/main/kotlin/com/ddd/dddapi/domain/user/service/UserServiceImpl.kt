package com.ddd.dddapi.domain.user.service

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
        val user = userRepository.findByUserKey(tempUserKey)
        return user ?: userRepository.save(UserEntity(userKey = tempUserKey))
    }
}