-- question column type - TEXT 변경
ALTER TABLE tarot_questions
    MODIFY COLUMN question TEXT NOT NULL;

-- entity 추가
CREATE TABLE tarot_result_follow_questions (
   id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',

   tarot_result_id BIGINT NOT NULL COMMENT '타로 결과 ID',

   questions_string TEXT NULL COMMENT '추천 꼬리질문들. \n 로 구분',

   created_at      datetime(6)  null,
   updated_at      datetime(6)  null,

   CONSTRAINT fk_tarot_result_follow_questions_tarot_result
       FOREIGN KEY (tarot_result_id) REFERENCES tarotnyang.tarot_results(id)
           ON DELETE CASCADE
);
    comment '타로결과에 대한 추천 꼬리질문';
