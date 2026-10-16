package com.bccard.munki.presentation.photo.favorite

import androidx.lifecycle.viewModelScope
import com.bccard.munki.domain.usecase.local.LikeUseCase
import com.bccard.munki.foundation.base.BaseEffect.Navigate
import com.bccard.munki.foundation.base.BaseViewModel
import com.bccard.munki.foundation.navigation.app.AppRoute
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteEffect
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ClickPhoto
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ToggleLike
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteState
import kotlinx.coroutines.launch

/**
 * Photo Favorite ViewModel
 *
 * 좋아요 목록 — API 없이 Room 만 구독해서 오프라인에서도 보인다
 *
 * @author 정문기
 */
class PhotoFavoriteViewModel(
    private val likeUseCase: LikeUseCase
) : BaseViewModel<PhotoFavoriteState, PhotoFavoriteEffect, PhotoFavoriteIntent>(PhotoFavoriteState()) {

    init {
        observeLikedPhotos()
    }

    // Intent ----------------------------------------------------------------------------------- //

    override fun handleIntent(event: PhotoFavoriteIntent) {
        when (event) {
            is ToggleLike -> viewModelScope.launch { likeUseCase.toggle(event.photo) }

            is ClickPhoto -> sendBaseEffect(Navigate(AppRoute.PhotoDetail(event.photoId)))
        }
    }

    // Handle ----------------------------------------------------------------------------------- //

    // 좋아요 목록 구독 — 하트를 끄면 DB 에서 지워지고 새 목록이 와서 카드가 사라진다
    private fun observeLikedPhotos() = viewModelScope.launch {
        likeUseCase.likedPhotos.collect { photos ->
            setState { it.copy(photos = photos, isLoading = false) }
        }
    }
}