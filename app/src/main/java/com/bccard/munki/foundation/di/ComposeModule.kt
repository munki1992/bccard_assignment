package com.bccard.munki.foundation.di

import com.bccard.munki.MainViewModel
import com.bccard.munki.presentation.sample.SampleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val composeModule = module {

    // 앱 전역 (Activity 레벨)
    viewModelOf(::MainViewModel)

    viewModelOf(::SampleViewModel)
}