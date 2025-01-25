package com.ddd.dddapi.domain.common.service

import com.ddd.dddapi.domain.common.entity.ShareLogEntity
import com.ddd.dddapi.domain.common.repository.ShareLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommonServiceImpl(
    private val shareLogRepository: ShareLogRepository
): ShareLogService {
    @Transactional
    override fun saveShareLog(tarotResultId: Long) {
        shareLogRepository.save(ShareLogEntity(tarotResultId = tarotResultId))
    }
}