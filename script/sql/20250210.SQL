-- 유저 테이블에 로그인 타입과 소셜 로그인 시 해당 소셜 내 유저 식별자를 추가합니다.
-- 1. login_type 컬럼 추가
ALTER TABLE users
    ADD COLUMN login_type VARCHAR(50) DEFAULT 'GUEST' COMMENT '유저 로그인 정보';

-- 2. social_id 컬럼 추가
ALTER TABLE users
    ADD COLUMN social_id VARCHAR(255) COMMENT '소셜 로그인 시 해당 소셜 내 유저 식별자';

-- 3. temp_user_key user_key로 변경
ALTER TABLE users
    CHANGE COLUMN temp_user_key user_key VARCHAR(36) NOT NULL UNIQUE COMMENT '사용자 고유 식별자 (UUID)';