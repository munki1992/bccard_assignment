package com.bccard.munki.presentation.photo.list

/**
 * Photo List Contract
 *
 * @author 정문기
 */
object PhotoListContract {

    class PhotoListState

    sealed interface PhotoListIntent {
        data class ClickPhoto(val photoId: String) : PhotoListIntent
    }

    sealed interface PhotoListEffect
}