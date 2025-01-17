package com.ddd.dddapi.domain.tarot.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment


@Comment("질문 & 결과의 인과관계 연결을 위한 테이블")
@Entity
@Table(name = "tarot_results")
class TarotResultEntity(
    @Id
    @OneToOne
    @JoinColumn(name = "question_message_id", nullable = false)
    val questionMessage: TarotChatMessageEntity,

    @Comment("질문에 대한 결과 메시지, 선택된 타로카드 이름을 가짐")
    @OneToOne
    @JoinColumn(name = "result_message_id", nullable = false)
    val resultMessage: TarotChatMessageEntity
)
