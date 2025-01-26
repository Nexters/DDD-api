package com.ddd.dddapi.domain.common.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Comment("공유하기 로그 테이블")
@Entity
@Table(name = "share_logs")
class ShareLogEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Comment("공유한 사용자 결과 id")
    @Column(name = "tarot_result_id", nullable = true)
    val tarotResultId: Long,
): BaseEntity()