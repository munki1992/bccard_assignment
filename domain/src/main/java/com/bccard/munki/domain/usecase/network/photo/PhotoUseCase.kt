package com.bccard.munki.domain.usecase.network.photo

import com.bccard.munki.domain.repository.network.photo.PhotoRepo

/**
 * Photo UseCase
 *
 * @author 정문기
 */
class PhotoUseCase(private val repo: PhotoRepo) {

    // 사진 목록 (페이지 단위)
    fun photoPagingData()                   = repo.photoPagingData()

    // 사진 상세
    suspend fun photoDetail(id: String)     = repo.photoDetail(id)
}