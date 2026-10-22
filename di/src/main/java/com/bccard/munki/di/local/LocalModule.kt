package com.bccard.munki.di.local

import androidx.room.Room
import com.bccard.munki.core.consts.Constants
import com.bccard.munki.data.local.AppDatabase
import com.bccard.munki.data.local.gallery.GallerySaveRepoImpl
import com.bccard.munki.data.local.like.LikeRepoImpl
import com.bccard.munki.data.local.like.LikedImageStore
import com.bccard.munki.data.network.image.ImageDownloader
import com.bccard.munki.domain.repository.local.GallerySaveRepo
import com.bccard.munki.domain.repository.local.LikeRepo
import com.bccard.munki.domain.usecase.local.GallerySaveUseCase
import com.bccard.munki.domain.usecase.local.LikeUseCase
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {

    // ===== Database =====
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, Constants.DATABASE_NAME).build()
    }

    single { get<AppDatabase>().likedPhotoDao() }

    // ===== Network (이미지 파일 다운로드용 — API Retrofit 과 별개) =====
    single {
        OkHttpClient.Builder()
//            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS))                // TLS 1.2 이상만 허용 (API Retrofit 과 같은 기준)
            .build()
    }
    single { ImageDownloader(get()) }                                          // GallerySaveRepo · LikedImageStore 공용

    // ===== File (좋아요 사진 오프라인용 이미지) =====
    single { LikedImageStore(androidContext(), get()) }                        // LikeRepo

    // ===== Repository =====
    single<LikeRepo> { LikeRepoImpl(get(), get()) }
    single<GallerySaveRepo> { GallerySaveRepoImpl(androidContext(), get()) }

    // ===== UseCase =====
    factory { LikeUseCase(get()) }
    factory { GallerySaveUseCase(get()) }
}