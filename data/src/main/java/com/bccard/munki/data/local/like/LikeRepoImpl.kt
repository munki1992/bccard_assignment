package com.bccard.munki.data.local.like

import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.repository.local.LikeRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Like Repo Impl
 *
 * @author 정문기
 */
class LikeRepoImpl(
    private val dao: LikedPhotoDao,
    private val imageStore: LikedImageStore
) : LikeRepo {

    // Val -------------------------------------------------------------------------------------- //


    // 이미지 파일 저장용 코루틴 스코프
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    // Override --------------------------------------------------------------------------------- //


    // 좋아요한 사진 ID 전체 — 목록 하트 표시용
    override val likedIds: Flow<Set<String>> = dao.observeLikedIds().map { it.toSet() }

    // 좋아요한 사진 목록 (최근 순) — 좋아요 목록 화면용
    override val likedPhotos: Flow<List<Photo>> = dao.observeLikedPhotos().map { list -> list.map { it.toPhoto() } }

    // 이 사진 하나가 좋아요인지 — 상세 하트 표시용
    override fun isLiked(id: String): Flow<Boolean> = dao.observeIsLiked(id)

    // 좋아요한 사진 하나 — 저장해 둔 칸만 채운 Photo (오프라인용 이미지 경로 포함)
    override suspend fun findLikedPhoto(id: String): Photo? = dao.getLikedPhoto(id)?.toPhoto()

    // 좋아요 토글 — 없으면 저장(+ 이미지 파일), 있으면 삭제(+ 이미지 파일)
    override suspend fun toggle(photo: Photo) {
        val id = photo.id ?: return
        val saved = dao.getLikedPhoto(id)

        if (saved != null)
            unlike(id, saved.localImagePath)
        else like(photo)
    }


    // Function --------------------------------------------------------------------------------- //


    // 하트 켬 — ① 줄 저장 (하트 즉시 반영) ② 이미지 파일을 받아 경로 기록 (뒤에서)
    private suspend fun like(photo: Photo) {
        val entity = photo.toLikedPhotoEntity() ?: return
        dao.insertLikedPhoto(entity)

        val url = photo.urls?.regular ?: return
        scope.launch {
            val path = imageStore.save(entity.id, url) ?: return@launch

            // 받는 동안 하트를 껐으면 줄이 없다 — 남은 파일을 지운다
            if (dao.getLikedPhoto(entity.id) == null)
                imageStore.delete(path)
            else dao.updateImagePath(entity.id, path)
        }
    }

    // 하트 끔 — 줄 삭제 후 저장해 둔 이미지 파일도 삭제
    private suspend fun unlike(id: String, imagePath: String?) {
        dao.deleteLikedPhoto(id)
        imageStore.delete(imagePath)
    }
}