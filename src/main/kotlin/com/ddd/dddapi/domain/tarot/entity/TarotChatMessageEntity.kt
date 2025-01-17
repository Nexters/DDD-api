package com.ddd.dddapi.domain.tarot.entity

import com.ddd.dddapi.domain.common.entity.BaseEntity
import com.ddd.dddapi.domain.user.entity.UserEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Comment

@Entity
@Table(name = "tarot_chat_messages")
class TarotChatMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_room_id", nullable = false)
    val chatRoom: TarotChatRoomEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    val messageType: MessageType,

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    val senderType: MessageSender,

    @Column(name = "message", nullable = false)
    val message: String,

    @Comment("메시지를 보낸 사용자, SYSTEM 메세지의 경우 null")
    @JoinColumn(name = "sender_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    val sender: UserEntity? = null,

    @Comment("유저가 선택한 타로카드 이름, MessageType == TAROT_RESULT 일 경우 사용")
    @Column(name = "tarot_name", nullable = true)
    val tarotName: String? = null,

    @Comment("추천 질문 ID, MessageType == RECOMMEND_TAROT_QUESTION 일 경우 존재")
    @Column(name = "reference_tarot_question_id", nullable = true)
    val referenceTarotQuestionId: Long? = null,
): BaseEntity()


