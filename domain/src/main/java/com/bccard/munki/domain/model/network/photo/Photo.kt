package com.bccard.munki.domain.model.network.photo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Photo
 *
 * 사진 1건 — 목록·상세 공용
 *
 * @author 정문기
 */
@JsonClass(generateAdapter = true)
data class Photo(
    val id: String? = null,                                                                         // 사진 ID
    val width: Int? = null,                                                                         // 원본 너비 (px)
    val height: Int? = null,                                                                        // 원본 높이 (px)
    val color: String? = null,                                                                      // 대표 색 (#RRGGBB) — 로딩 중 배경
    val description: String? = null,                                                                // 설명
    val likes: Int? = null,                                                                         // 좋아요 수
    val urls: PhotoUrls? = null,                                                                    // 크기별 이미지 URL
    val links: PhotoLinks? = null,                                                                  // 관련 링크
    val user: PhotoUser? = null,                                                                    // 작가
    @param:Json(name = "blur_hash") val blurHash: String? = null,                                   // BlurHash — 로딩 중 흐린 미리보기
    @param:Json(name = "alt_description") val altDescription: String? = null,                       // 대체 텍스트 (접근성)

    // 상세 전용
    val views: Long? = null,                                                                        // 조회 수
    val downloads: Long? = null,                                                                    // 다운로드 수
    val exif: PhotoExif? = null,                                                                    // 촬영 정보
    val location: PhotoLocation? = null,                                                            // 촬영 위치
    val tags: List<PhotoTag>? = null,                                                               // 태그

    // 앱 전용 (API 응답에 없음 — JSON 변환에서 제외)
    @Transient val localImagePath: String? = null                                                   // 좋아요 사진의 오프라인용 이미지 파일 경로 (Room 에서만 채운다)
)

@JsonClass(generateAdapter = true)
data class PhotoUrls(
    val raw: String? = null,                                                                        // 원본 (가공 파라미터용)
    val full: String? = null,                                                                       // 원본 크기 JPG
    val regular: String? = null,                                                                    // 너비 1080
    val small: String? = null,                                                                      // 너비 400
    val thumb: String? = null                                                                       // 너비 200
)

@JsonClass(generateAdapter = true)
data class PhotoLinks(
    val html: String? = null,                                                                       // Unsplash 웹 페이지
    val download: String? = null,                                                                   // 다운로드 페이지
    @param:Json(name = "download_location") val downloadLocation: String? = null                    // 다운로드 추적 API URL
)

@JsonClass(generateAdapter = true)
data class PhotoUser(
    val id: String? = null,                                                                         // 작가 ID
    val username: String? = null,                                                                   // 아이디
    val name: String? = null,                                                                       // 이름
    @param:Json(name = "profile_image") val profileImage: ProfileImage? = null                      // 프로필 이미지
)

@JsonClass(generateAdapter = true)
data class ProfileImage(
    val small: String? = null,                                                                      // 32px
    val medium: String? = null,                                                                     // 64px
    val large: String? = null                                                                       // 128px
)

@JsonClass(generateAdapter = true)
data class PhotoExif(
    val make: String? = null,                                                                       // 제조사
    val model: String? = null,                                                                      // 기종
    val name: String? = null,                                                                       // 표시 이름
    val aperture: String? = null,                                                                   // 조리개
    val iso: Int? = null,                                                                           // ISO
    @param:Json(name = "exposure_time") val exposureTime: String? = null,                           // 셔터 속도
    @param:Json(name = "focal_length") val focalLength: String? = null                              // 초점 거리
)

@JsonClass(generateAdapter = true)
data class PhotoLocation(
    val name: String? = null,                                                                       // 표시 이름
    val city: String? = null,                                                                       // 도시
    val country: String? = null,                                                                    // 국가
    val position: PhotoPosition? = null                                                             // 좌표
)

@JsonClass(generateAdapter = true)
data class PhotoPosition(
    val latitude: Double? = null,                                                                   // 위도
    val longitude: Double? = null                                                                   // 경도
)

@JsonClass(generateAdapter = true)
data class PhotoTag(
    val type: String? = null,                                                                       // 태그 종류 (search·landing_page)
    val title: String? = null                                                                       // 태그 이름
)

// 가로 ÷ 세로 비율 — 크기를 모르거나 0 이면 null
// 예) 4000×3000 → 1.33 (가로가 긴 사진), 3000×4000 → 0.75 (세로가 긴 사진)
fun Photo.aspectRatio(): Float? {
    val w = width?.takeIf { it > 0 } ?: return null
    val h = height?.takeIf { it > 0 } ?: return null
    return w.toFloat() / h
}