package com.bccard.munki.foundation.di

import com.bccard.munki.MainViewModel
import com.bccard.munki.presentation.photo.detail.PhotoDetailViewModel
import com.bccard.munki.presentation.photo.list.PhotoListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val composeModule = module {

    // 앱 전역 (Activity 레벨)
    viewModelOf(::MainViewModel)

    // 사진 (목록, 상세, 좋아요 목록)
    viewModelOf(::PhotoListViewModel)
    viewModelOf(::PhotoDetailViewModel)
}