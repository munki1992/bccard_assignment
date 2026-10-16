package com.bccard.munki.domain.usecase.local

import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.repository.local.LikeRepo

/**
 * Like UseCase
 *
 * @author 정문기
 */
class LikeUseCase(private val repo: LikeRepo) {

    // 좋아요한 사진 ID
    val likedIds = repo.likedIds

    // 좋아요한 사진 목록 (최근 순)
    val likedPhotos = repo.likedPhotos

    // 이 사진 하나가 좋아요인지
    fun isLiked(id: String) = repo.isLiked(id)

    // 좋아요한 사진 하나 (없으면 null)
    suspend fun findLikedPhoto(id: String) = repo.findLikedPhoto(id)

    // 좋아요 토글
    suspend fun toggle(photo: Photo) = repo.toggle(photo)
}