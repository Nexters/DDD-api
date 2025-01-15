package com.ddd.dddapi.external.ai.client

interface AiServerClient {
    /**
     * 메세지의 유형을 분류
     */
    fun messageClassification()

    /**
     * 일반 대화 메세지
     */
    fun normalMessage()

    /**
     * 질문 메세지
     */
    fun questionMessage()

    /**
     * 꼬리 질문 메세지
     */
    fun followQuestionMessage()
}