package com.ddd.dddapi.domain.user.entity

import com.ddd.dddapi.domain.common.entity.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.Comment


@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Comment("인증 도입 전 임시 사용자 키")
    @Size(max = 255)
    @Column(name = "temp_user_key")
    var tempUserKey: String? = null,
): BaseEntity()