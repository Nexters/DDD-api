package com.ddd.dddapi.domain.tarot.entity

import com.ddd.dddapi.common.enums.TarotInfo
import com.ddd.dddapi.domain.chat.entity.TarotChatMessageEntity
import com.ddd.dddapi.domain.common.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment


@Comment("질문 & 결과의 인과관계 연결을 위한 테이블")
@Entity
@Table(name = "tarot_results")
class TarotResultEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Comment("유저가 선택한 타로카드(Enum으로 관리), MessageType == SYSTEM_SELECTED_TAROT 일 경우 사용")
    @Enumerated(EnumType.STRING)
    @Column(name = "tarot", nullable = false, columnDefinition = "VARCHAR(50)")
    val tarot: TarotInfo,

    @Comment("타로결과 유형")
    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(50)")
    val type: String,

    @Column(name = "card_value_summary", nullable = false, columnDefinition = "VARCHAR(255)")
    val cardValueSummary: String,

    @Lob
    @Column(name = "card_value_description", nullable = false, columnDefinition = "TEXT")
    val cardValueDescription: String,

    @Column(name = "answer_summary", nullable = false, columnDefinition = "VARCHAR(255)")
    val answerSummary: String,

    @Lob
    @Column(name = "answer_description", nullable = false, columnDefinition = "TEXT")
    val answerDescription: String,

    @Column(name = "advice_summary", nullable = false, columnDefinition = "VARCHAR(255)")
    val adviceSummary: String,

    @Lob
    @Column(name = "advice_description", nullable = false, columnDefinition = "TEXT")
    val adviceDescription: String,

    @Lob
    @Column(name = "comprehensive_summary", nullable = false, columnDefinition = "TEXT")
    val comprehensiveSummary: String
): BaseEntity()
