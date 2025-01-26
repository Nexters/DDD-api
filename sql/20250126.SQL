create table ddd.share_logs
(
    id              bigint auto_increment
        primary key,
    created_at      datetime(6) null,
    updated_at      datetime(6) null,
    tarot_result_id bigint      null comment '공유한 사용자 결과 id'
)
    comment '공유하기 로그 테이블';

create table ddd.tarot_questions
(
    id              bigint auto_increment
        primary key,
    created_at      datetime(6)  null,
    updated_at      datetime(6)  null,
    question        varchar(255) not null,
    reference_count bigint       not null comment '해당 질문을 사용한 횟수'
)
    comment '유저들이 작성한 타로질문';

create table ddd.tarot_results
(
    id                     bigint auto_increment
        primary key,
    created_at             datetime(6)  null,
    updated_at             datetime(6)  null,
    advice_description     text         not null,
    advice_summary         varchar(255) not null,
    answer_description     text         not null,
    answer_summary         varchar(255) not null,
    card_value_description text         not null,
    card_value_summary     varchar(255) not null,
    tarot                  varchar(50)  not null comment '유저가 선택한 타로카드(Enum으로 관리), MessageType == SYSTEM_SELECTED_TAROT 일 경우 사용',
    type                   varchar(50)  not null comment '타로결과 유형'
)
    comment '질문 & 결과의 인과관계 연결을 위한 테이블';

create table ddd.users
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6)  null,
    updated_at    datetime(6)  null,
    temp_user_key varchar(255) null comment '인증 도입 전 임시 사용자 키'
);

create table ddd.tarot_chat_rooms
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    user_id    bigint      not null,
    constraint FKfetrndhgss9skr5uts2bfi318
        foreign key (user_id) references ddd.users (id)
            on delete cascade
);

create table ddd.tarot_chat_messages
(
    id                          bigint auto_increment
        primary key,
    created_at                  datetime(6) null,
    updated_at                  datetime(6) null,
    message                     text        not null,
    message_type                varchar(50) not null comment '메세지의 의미, MessageType Enum 참조',
    reference_tarot_question_id bigint      null comment '추천 질문 ID, MessageType == RECOMMEND_TAROT_QUESTION 일 경우 존재',
    sender_type                 varchar(50) not null,
    tarot                       varchar(50) null comment '유저가 선택한 타로카드(Enum으로 관리), MessageType == SYSTEM_SELECTED_TAROT 일 경우 사용',
    chat_room_id                bigint      not null,
    sender_id                   bigint      null comment '메시지를 보낸 사용자, MessageSender == SYSTEM 일 경우 null',
    tarot_result_id             bigint      null comment '타로 결과 ID, MessageType == SYSTEM_TAROT_RESULT 일 경우 존재',
    constraint UKs0q4mxkb48u1ixygohngj4u36
        unique (tarot_result_id),
    constraint FK9p1n9pu4qc0j6ingith4idst3
        foreign key (tarot_result_id) references ddd.tarot_results (id),
    constraint FKbfpg6llb60ha84bb6uruacl97
        foreign key (sender_id) references ddd.users (id),
    constraint FKoku8whksvwouj9pshqbp0edbd
        foreign key (chat_room_id) references ddd.tarot_chat_rooms (id)
);

