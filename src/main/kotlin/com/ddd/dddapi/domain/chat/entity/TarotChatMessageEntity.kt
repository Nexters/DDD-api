package com.ddd.dddapi.domain.chat.entity

import com.ddd.dddapi.common.enums.MessageSender
import com.ddd.dddapi.common.enums.MessageType
import com.ddd.dddapi.common.enums.TarotInfo
import com.ddd.dddapi.domain.common.entity.BaseEntity
import com.ddd.dddapi.domain.tarot.entity.TarotResultEntity
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

    @Comment("메세지의 의미, MessageType Enum 참조")
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, columnDefinition = "VARCHAR(50)")
    val messageType: MessageType,

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false, columnDefinition = "VARCHAR(50)")
    val senderType: MessageSender,

    @Lob
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    val message: String,

    @Comment("메시지를 보낸 사용자, MessageSender == SYSTEM 일 경우 null")
    @JoinColumn(name = "sender_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    val sender: UserEntity? = null,

    @Comment("유저가 선택한 타로카드(Enum으로 관리), MessageType == SYSTEM_SELECTED_TAROT 일 경우 사용")
    @Enumerated(EnumType.STRING)
    @Column(name = "tarot", nullable = true, columnDefinition = "VARCHAR(50)")
    val tarot: TarotInfo? = null,

    @Comment("타로 결과 ID, MessageType == SYSTEM_TAROT_RESULT 일 경우 존재")
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tarot_result_id", nullable = true)
    val tarotResult: TarotResultEntity? = null,

    @Comment("추천 질문 ID, MessageType == RECOMMEND_TAROT_QUESTION 일 경우 존재")
    @Column(name = "reference_tarot_question_id", nullable = true)
    val referenceTarotQuestionId: Long? = null,
): BaseEntity() {
    companion object {
        fun createTarotResultChatMessage(
            chatRoom: TarotChatRoomEntity,
            tarot: TarotInfo? = null,
            tarotResult: TarotResultEntity? = null,
        ): TarotChatMessageEntity {
            return TarotChatMessageEntity(
                chatRoom = chatRoom,
                messageType = MessageType.SYSTEM_TAROT_RESULT,
                senderType = MessageSender.SYSTEM,
                message = "타로 결과를 다시 보고 싶다면 카드를 눌러봐\n또 궁금한거 있어냥?",
                tarot = tarot,
                tarotResult = tarotResult
            )
        }

        fun createWelcomeChatMessage(
            chatRoom: TarotChatRoomEntity
        ): TarotChatMessageEntity {
            return TarotChatMessageEntity(
                chatRoom = chatRoom,
                messageType = MessageType.SYSTEM_HELLO,
                senderType = MessageSender.SYSTEM,
                message = "안녕 집사 🐾\n따뜻한 마룻바닥이 그리운 겨울 밤이야\n오늘은 어떤게 궁금해서 찾아왔어냥?"
            )
        }
    }
}


