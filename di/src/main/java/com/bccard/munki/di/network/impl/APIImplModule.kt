package com.bccard.munki.di.network.impl

import com.bccard.munki.data.network.impl.photo.PhotoRepoImpl
import com.bccard.munki.domain.repository.network.photo.PhotoRepo
import com.bccard.munki.domain.usecase.network.photo.PhotoUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val apiImplModule = module {

    // ===== Repository (domain 인터페이스 ↔ data 구현체) =====
    single<PhotoRepo> { PhotoRepoImpl(androidContext(), get()) }

    // ===== UseCase =====
    factory { PhotoUseCase(get()) }
}