package com.bccard.munki.fake

import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.repository.local.LikeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Fake Like Repo
 *
 * Room 대신 메모리(List)에 좋아요한 사진을 저장하는 가짜 LikeRepo
 *
 * @author 정문기
 */
class FakeLikeRepo(liked: List<Photo> = emptyList()) : LikeRepo {

    // 좋아요한 사진 (최근 순)
    private val photos = MutableStateFlow(liked)

    override val likedIds: Flow<Set<String>> = photos.map { list -> list.mapNotNull { it.id }.toSet() }

    override val likedPhotos: Flow<List<Photo>> = photos

    override fun isLiked(id: String): Flow<Boolean> = photos.map { list -> list.any { it.id == id } }

    override suspend fun findLikedPhoto(id: String): Photo? = photos.value.find { it.id == id }

    // 있으면 빼고, 없으면 맨 앞에 넣는다
    override suspend fun toggle(photo: Photo) = photos.update { list ->
        if (list.any { it.id == photo.id })
            list.filterNot { it.id == photo.id }
        else listOf(photo) + list
    }
}