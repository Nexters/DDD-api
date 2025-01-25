package com.ddd.dddapi.domain.chat.entity

import com.ddd.dddapi.domain.common.entity.BaseEntity
import com.ddd.dddapi.domain.user.entity.UserEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "tarot_chat_rooms")
class TarotChatRoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
): BaseEntity()