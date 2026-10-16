package com.bccard.munki.data.local.like

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Liked Photo Entity
 *
 * @author 정문기
 */
@Entity(tableName = "liked_photo")
data class LikedPhotoEntity(
    @PrimaryKey
    val id: String,                                                                                 // 사진 ID — 같은 사진은 한 번만 저장 (기본 키)

    val width: Int?,                                                                                // 원본 너비 (px)
    val height: Int?,                                                                               // 원본 높이 (px)
    val color: String?,                                                                             // 대표 색 — 이미지 로딩 전 배경
    val description: String?,                                                                       // 설명
    @ColumnInfo(name = "alt_description") val altDescription: String?,                              // 대체 텍스트
    @ColumnInfo(name = "small_url") val smallUrl: String?,                                          // 목록용 이미지 URL (너비 400)
    @ColumnInfo(name = "regular_url") val regularUrl: String?,                                      // 상세용 이미지 URL (너비 1080)
    @ColumnInfo(name = "user_name") val userName: String?,                                          // 작가 이름
    @ColumnInfo(name = "user_username") val userUsername: String?,                                  // 작가 아이디
    @ColumnInfo(name = "user_profile_url") val userProfileUrl: String?,                             // 작가 프로필 이미지 URL
    @ColumnInfo(name = "liked_at") val likedAt: Long,                                               // 좋아요 누른 시각 (ms) — 최근 순 정렬용
    @ColumnInfo(name = "local_image_path") val localImagePath: String? = null                       // 오프라인용 이미지 파일 경로
)