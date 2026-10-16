package com.bccard.munki.data.local.like

import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoUrls
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.domain.model.network.photo.ProfileImage

/**
 * Liked Photo Mapper
 *
 * @author 정문기
 */

// Photo → LikedPhotoEntity (하트 켤 때 DB 에 저장할 모양으로)
// 좋아요 누른 시각을 함께 기록한다. id 가 없으면 저장할 수 없어 null
fun Photo.toLikedPhotoEntity(likedAt: Long = System.currentTimeMillis()): LikedPhotoEntity? = id?.let {
    LikedPhotoEntity(
        id             = it,
        width          = width,
        height         = height,
        color          = color,
        description    = description,
        altDescription = altDescription,
        smallUrl       = urls?.small,
        regularUrl     = urls?.regular,
        userName       = user?.name,
        userUsername   = user?.username,
        userProfileUrl = user?.profileImage?.medium,
        likedAt        = likedAt
    )
}

// LikedPhotoEntity → Photo (DB 에서 꺼내 화면에 쓸 모양으로)
// 저장해 둔 칸과 오프라인 이미지 경로만 채운다. 조회수·태그 등 저장 안 한 값은 null
fun LikedPhotoEntity.toPhoto(): Photo = Photo(
    id             = id,
    width          = width,
    height         = height,
    color          = color,
    description    = description,
    altDescription = altDescription,
    urls           = PhotoUrls(small = smallUrl, regular = regularUrl),
    user           = PhotoUser(name = userName, username = userUsername, profileImage = ProfileImage(medium = userProfileUrl)),
    localImagePath = localImagePath
)