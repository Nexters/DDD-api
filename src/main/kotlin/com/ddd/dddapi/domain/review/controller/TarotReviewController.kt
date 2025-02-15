package com.ddd.dddapi.domain.review.controller

import com.ddd.dddapi.common.annotation.RequestUser
import com.ddd.dddapi.common.dto.RequestUserInfo
import com.ddd.dddapi.domain.review.dto.CreateTarotReviewRequestDto
import com.ddd.dddapi.domain.review.dto.HasReviewedTarotResponseDto
import com.ddd.dddapi.domain.review.service.TarotReviewService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/review")
@Tag(name = "03. Review API")
class TarotReviewController(
    private val tarotReviewService: TarotReviewService,
) {
    @GetMapping("/exist/{tarotResultId}")
    fun hasReviewedTarot(
        @RequestUser requestUser: RequestUserInfo,
        @PathVariable tarotResultId: Long,
    ): HasReviewedTarotResponseDto {
        return HasReviewedTarotResponseDto(tarotReviewService.hasReviewedTarot(requestUser.userKey, tarotResultId))
    }

    @PostMapping("/create")
    fun createTarotReview(
        @RequestUser requestUser: RequestUserInfo,
        @RequestBody createTarotReviewRequestDto: CreateTarotReviewRequestDto,
    ) {
        tarotReviewService.createTarotReview(requestUser.userKey, createTarotReviewRequestDto.tarotResultId, createTarotReviewRequestDto.grade)
    }
}