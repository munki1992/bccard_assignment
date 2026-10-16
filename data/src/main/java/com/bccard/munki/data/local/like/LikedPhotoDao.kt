package com.bccard.munki.data.local.like

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Liked Photo - Data Access Object
 *
 * @author 정문기
 */
@Dao
interface LikedPhotoDao {

    // 좋아요 목록 (최근에 누른 순) — 테이블이 바뀔 때마다 새 목록을 흘려준다
    @Query("SELECT * FROM liked_photo ORDER BY liked_at DESC")
    fun observeLikedPhotos(): Flow<List<LikedPhotoEntity>>

    // 좋아요한 사진 ID 만 — 목록 하트 표시용
    @Query("SELECT id FROM liked_photo")
    fun observeLikedIds(): Flow<List<String>>

    // 이 사진 하나가 좋아요인지 — 상세 하트 표시용 (있으면 true)
    @Query("SELECT EXISTS(SELECT 1 FROM liked_photo WHERE id = :id)")
    fun observeIsLiked(id: String): Flow<Boolean>

    // 저장 — 같은 id 가 이미 있으면 새 값으로 덮어쓴다
    // onConflict -> 만약에 같은 값이 들어오면 덮어쓴다는 뜻
    // OnConflictStrategy.REPLACE -> 만약에 이미 저장된 값이 있다면 새 값으로 덮어쓴다는 뜻
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedPhoto(photo: LikedPhotoEntity)

    // 삭제 — id 가 같은 행을 지운다
    @Query("DELETE FROM liked_photo WHERE id = :id")
    suspend fun deleteLikedPhoto(id: String)

    // id 로 한 줄 꺼내기 — 없으면 null (상세 오프라인 표시, 삭제 전 이미지 파일 경로 확인용)
    @Query("SELECT * FROM liked_photo WHERE id = :id")
    suspend fun getLikedPhoto(id: String): LikedPhotoEntity?

    // 오프라인용 이미지 파일 경로 기록 — id 가 같은 행의 local_image_path 칸만 바꾼다
    @Query("UPDATE liked_photo SET local_image_path = :path WHERE id = :id")
    suspend fun updateImagePath(id: String, path: String?)
}