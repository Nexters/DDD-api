package com.ddd.dddapi.domain.common.repository

import com.ddd.dddapi.domain.common.entity.ShareLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ShareLogRepository: JpaRepository<ShareLogEntity, Long> {
}