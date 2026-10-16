package com.bccard.munki.presentation.photo.list

import com.bccard.munki.domain.model.network.photo.Photo

/**
 * Photo List Contract
 *
 * @author 정문기
 */
object PhotoListContract {

    data class PhotoListState(
        val likedIds: Set<String> = emptySet()                                                      // 좋아요한 사진 ID
    )

    sealed interface PhotoListIntent {
        data class ToggleLike(val photo: Photo) : PhotoListIntent

        data class ClickPhoto(val photoId: String) : PhotoListIntent

        data object ClickFavorites : PhotoListIntent
    }

    sealed interface PhotoListEffect
}