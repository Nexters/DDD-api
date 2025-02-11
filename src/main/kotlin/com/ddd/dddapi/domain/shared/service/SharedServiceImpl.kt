package com.ddd.dddapi.domain.shared.service

import com.ddd.dddapi.domain.shared.entity.ShareLogEntity
import com.ddd.dddapi.domain.shared.repository.ShareLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SharedServiceImpl(
    private val shareLogRepository: ShareLogRepository
): ShareLogService {
    @Transactional
    override fun saveShareLog(tarotResultId: Long) {
        shareLogRepository.save(ShareLogEntity(tarotResultId = tarotResultId))
    }
}