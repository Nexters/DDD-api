package com.ddd.dddapi.domain.shared.repository

import com.ddd.dddapi.domain.shared.entity.ShareLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ShareLogRepository: JpaRepository<ShareLogEntity, Long> {
}