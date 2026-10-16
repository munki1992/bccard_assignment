package com.bccard.munki.presentation.photo.favorite

import com.bccard.munki.domain.model.network.photo.Photo

/**
 * Photo Favorite Contract
 *
 * @author 정문기
 */
object PhotoFavoriteContract {

    data class PhotoFavoriteState(
        val photos: List<Photo> = emptyList(),                                                      // 좋아요한 사진 (Room, 최근 순)
        val isLoading: Boolean = true                                                               // 첫 목록을 받기 전 — 빈 상태 문구가 깜빡이지 않게
    )

    sealed interface PhotoFavoriteIntent {
        data class ToggleLike(val photo: Photo) : PhotoFavoriteIntent

        data class ClickPhoto(val photoId: String) : PhotoFavoriteIntent
    }

    sealed interface PhotoFavoriteEffect
}