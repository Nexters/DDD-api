create table tarot_questions
(
    id              bigint auto_increment
        primary key,
    created_at      datetime(6)  null,
    updated_at      datetime(6)  null,
    question        varchar(255) not null,
    reference_count bigint       not null comment '해당 질문을 사용한 횟수'
)
    comment '유저들이 작성한 타로질문';

create table users
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6)  null,
    updated_at    datetime(6)  null,
    temp_user_key varchar(255) null comment '인증 도입 전 임시 사용자 키'
);

create table tarot_chat_rooms
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    user_id    bigint      not null,
    constraint FKfetrndhgss9skr5uts2bfi318
        foreign key (user_id) references users (id)
            on delete cascade
);

create table tarot_chat_messages
(
    id                          bigint auto_increment
        primary key,
    created_at                  datetime(6)  null,
    updated_at                  datetime(6)  null,
    message                     varchar(255) not null,
    message_type                varchar(50)  not null,
    reference_tarot_question_id bigint       null comment '추천 질문 ID, MessageType == RECOMMEND_TAROT_QUESTION 일 경우 존재',
    sender_type                 varchar(50)  not null,
    tarot_name                  varchar(255) null comment '유저가 선택한 타로카드 이름, MessageType == TAROT_RESULT 일 경우 사용',
    chat_room_id                bigint       not null,
    sender_id                   bigint       null comment '메시지를 보낸 사용자, SYSTEM 메세지의 경우 null',
    tarot                       varchar(50)  null comment '유저가 선택한 타로카드(Enum으로 관리), MessageType == TAROT_RESULT 일 경우 사용',
    constraint FKbfpg6llb60ha84bb6uruacl97
        foreign key (sender_id) references users (id),
    constraint FKoku8whksvwouj9pshqbp0edbd
        foreign key (chat_room_id) references tarot_chat_rooms (id)
);

create table tarot_results
(
    question_message_id bigint not null
        primary key,
    result_message_id   bigint not null comment '질문에 대한 결과 메시지, 선택된 타로카드 이름을 가짐',
    constraint UK7ptc89oar6g3r41f3aqkm8av9
        unique (result_message_id),
    constraint FK97rr9hrn7sc3nmaglxuh398s5
        foreign key (question_message_id) references tarot_chat_messages (id),
    constraint FKqf43s1p31l164p7r0mdshv837
        foreign key (result_message_id) references tarot_chat_messages (id)
)
    comment '질문 & 결과의 인과관계 연결을 위한 테이블';

