package com.bccard.munki.presentation.photo.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.usecase.network.photo.PhotoUseCase
import com.bccard.munki.foundation.base.BaseEffect.Navigate
import com.bccard.munki.foundation.base.BaseViewModel
import com.bccard.munki.foundation.navigation.app.AppRoute
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListEffect
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ClickPhoto
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListState
import kotlinx.coroutines.flow.Flow

/**
 * Photo List ViewModel
 *
 * @author 정문기
 */
class PhotoListViewModel(
    photoUseCase: PhotoUseCase
) : BaseViewModel<PhotoListState, PhotoListEffect, PhotoListIntent>(PhotoListState()) {

    // 사진 목록 (페이지 단위) (ViewModel 구독)
    val photos: Flow<PagingData<Photo>> = photoUseCase.photoPagingData().cachedIn(viewModelScope)

    // Intent ----------------------------------------------------------------------------------- //

    override fun handleIntent(event: PhotoListIntent) {
        when (event) {
            is ClickPhoto  -> sendBaseEffect(Navigate(AppRoute.PhotoDetail(event.photoId)))
        }
    }
}