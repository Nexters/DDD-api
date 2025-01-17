package com.ddd.dddapi.domain.tarot.entity

import com.ddd.dddapi.domain.common.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Comment("유저들이 작성한 타로질문")
@Entity
@Table(name = "tarot_questions")
class TarotQuestionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "question", nullable = false)
    val question: String,

    @Comment("해당 질문을 사용한 횟수")
    @Column(name = "reference_count", nullable = false)
    val referenceCount: Long = 1,
): BaseEntity()