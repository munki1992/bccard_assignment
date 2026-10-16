package com.bccard.munki.domain.repository.local

import com.bccard.munki.domain.model.network.photo.Photo
import kotlinx.coroutines.flow.Flow

/**
 * Like Repo
 *
 * @author 정문기
 */
interface LikeRepo {

    // 좋아요한 사진 ID 전체 — 목록 하트 표시용
    val likedIds: Flow<Set<String>>

    // 좋아요한 사진 목록 (최근 순) — 좋아요 목록 화면용
    val likedPhotos: Flow<List<Photo>>

    // 이 사진 하나가 좋아요인지 — 상세 하트 표시용
    fun isLiked(id: String): Flow<Boolean>

    // 좋아요한 사진 하나 — 없으면 null (상세 오프라인 표시용)
    suspend fun findLikedPhoto(id: String): Photo?

    // 좋아요 토글 — 없으면 저장, 있으면 삭제
    suspend fun toggle(photo: Photo)
}