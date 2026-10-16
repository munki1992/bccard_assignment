package com.bccard.munki.presentation.photo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bccard.munki.R
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.domain.usecase.local.GallerySaveUseCase
import com.bccard.munki.domain.usecase.local.LikeUseCase
import com.bccard.munki.domain.usecase.network.photo.PhotoUseCase
import com.bccard.munki.foundation.base.BaseEffect.ShowToast
import com.bccard.munki.foundation.base.BaseViewModel
import com.bccard.munki.foundation.navigation.app.AppRoute
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailEffect
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.Download
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.DownloadPermissionDenied
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
    private val photoUseCase: PhotoUseCase,
    private val likeUseCase: LikeUseCase,
    private val gallerySaveUseCase: GallerySaveUseCase
) : BaseViewModel<PhotoDetailState, PhotoDetailEffect, PhotoDetailIntent>(PhotoDetailState()) {

    // 목록에서 넘겨준 사진 ID (AppRoute.PhotoDetail 인자)
    private val photoId = savedStateHandle.toRoute<AppRoute.PhotoDetail>().photoId

    init {
        observeLike()
        loadDetail()
    }

    // Intent ----------------------------------------------------------------------------------- //

    override fun handleIntent(event: PhotoDetailIntent) {
        when (event) {
            ToggleLike               -> state.value.photo?.let { photo -> viewModelScope.launch { likeUseCase.toggle(photo) } }

            Download                 -> download()

            DownloadPermissionDenied -> sendBaseEffect(ShowToast(R.string.msg_photo_detail_download_permission))
        }
    }

    // Handle ----------------------------------------------------------------------------------- //

    // 사진 상세
    private fun loadDetail() = viewModelScope.launch {
        // 좋아요한 사진이면 저장해 둔 정보로 먼저 그린다 — 오프라인이어도 화면이 나온다
        likeUseCase.findLikedPhoto(photoId)?.let { local ->
            setState { it.copy(photo = local, isLoading = false) }
        }

        // API — 성공하면 전체 정보(조회수·태그 등)로 교체, 실패하면 화면을 그대로 둔다
        when (val result = photoUseCase.photoDetail(photoId)) {
            is ApiResult.Success -> setState {
                it.copy(photo = result.data.copy(localImagePath = it.photo?.localImagePath), isLoading = false)
            }

            is ApiResult.Failure -> {
                setState { it.copy(isLoading = false) }

                if (state.value.photo == null)
                    handleCommonError(result.error)
            }
        }
    }

    // 다운로드
    private fun download() {
        val photo = state.value.photo ?: return
        if (state.value.isDownloading) return

        setState { it.copy(isDownloading = true) }

        viewModelScope.launch {
            when (val result = photoUseCase.photoDownload(photo)) {
                is ApiResult.Success -> {
                    val saved = result.data.url?.let { url -> gallerySaveUseCase.save(url, "unsplash_${photo.id}.jpg") } ?: false
                    sendBaseEffect(ShowToast(if (saved) R.string.msg_photo_detail_download_done else R.string.msg_photo_detail_download_fail))
                }

                is ApiResult.Failure -> handleCommonError(result.error)
            }

            setState { it.copy(isDownloading = false) }
        }
    }

    // 좋아요 상태 구독
    private fun observeLike() = viewModelScope.launch {
        likeUseCase.isLiked(photoId).collect { liked -> setState { it.copy(isLiked = liked) } }
    }
}