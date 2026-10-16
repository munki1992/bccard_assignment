package com.bccard.munki.presentation.photo.detail

import com.bccard.munki.domain.model.network.photo.Photo

/**
 * Photo Detail Contract
 *
 * @author 정문기
 */
object PhotoDetailContract {

    data class PhotoDetailState(
        val photo: Photo? = null,                                                                   // 상세 응답 (불러오기 전 null)
        val isLoading: Boolean = true,                                                              // 상세 요청 중 — 화면 가운데 로딩 표시
        val isLiked: Boolean = false,                                                               // 좋아요 여부 (LikeUseCase 구독)
        val isDownloading: Boolean = false                                                          // 다운로드 중 — 버튼 자리에 로딩 표시, 중복 클릭 방지
    )

    sealed interface PhotoDetailIntent {
        data object ToggleLike : PhotoDetailIntent
    }

    sealed interface PhotoDetailEffect
}