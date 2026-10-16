package com.bccard.munki.presentation.photo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.domain.usecase.network.photo.PhotoUseCase
import com.bccard.munki.foundation.base.BaseViewModel
import com.bccard.munki.foundation.navigation.app.AppRoute
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailEffect
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.ToggleLike
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailState
import kotlinx.coroutines.launch

/**
 * Photo Detail ViewModel
 *
 * @author 정문기
 */
class PhotoDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val photoUseCase: PhotoUseCase
) : BaseViewModel<PhotoDetailState, PhotoDetailEffect, PhotoDetailIntent>(PhotoDetailState()) {

    // 목록에서 넘겨준 사진 ID (AppRoute.PhotoDetail 인자)
    private val photoId = savedStateHandle.toRoute<AppRoute.PhotoDetail>().photoId

    init {
        loadDetail()
    }

    // Intent ----------------------------------------------------------------------------------- //

    override fun handleIntent(event: PhotoDetailIntent) {
        when (event) {
            ToggleLike -> {}
        }
    }

    // Handle ----------------------------------------------------------------------------------- //

    // 사진 상세
    private fun loadDetail() = viewModelScope.launch {
        when (val result = photoUseCase.photoDetail(photoId)) {
            is ApiResult.Success -> setState {
                it.copy(photo = result.data, isLoading = false)
            }

            is ApiResult.Failure -> {
                setState { it.copy(isLoading = false) }

                if (state.value.photo == null)
                    handleCommonError(result.error)
            }
        }
    }
}