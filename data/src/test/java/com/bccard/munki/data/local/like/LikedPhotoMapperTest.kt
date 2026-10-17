package com.bccard.munki.data.local.like

import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoTag
import com.bccard.munki.domain.model.network.photo.PhotoUrls
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.domain.model.network.photo.ProfileImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Liked Photo Mapper Test
 *
 * @author 정문기
 */
class LikedPhotoMapperTest {

    // API 에서 받은 '사진'
    private val photo = Photo(
        id = "photo-1",
        width = 4000,
        height = 3000,
        color = "#3C3C47",
        description = "설명",
        altDescription = "부가설명",
        urls = PhotoUrls(small = "small-url", regular = "regular-url", full = "full-url"),
        user = PhotoUser(name = "정문기", username = "정문기", profileImage = ProfileImage(medium = "profile-url")),
        views = 100,
        tags = listOf(PhotoTag(title = "테스트"))
    )

    // 사진을 Room에 저장한 '엔티티'
    private val entity = LikedPhotoEntity(
        id = "photo-1",
        width = 4000,
        height = 3000,
        color = "#3C3C47",
        description = "설명",
        altDescription = "부가설명",
        smallUrl = "small-url",
        regularUrl = "regular-url",
        userName = "정문기",
        userUsername = "정문기",
        userProfileUrl = "profile-url",
        likedAt = 100L,
        localImagePath = "/files/unsplash_liked/photo-1.jpg"
    )

    // Test ------------------------------------------------------------------------------------- //

    // assertNull   -> 값이 null 이면 통과, 아니면 실패
    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패

    // Test ------------------------------------------------------------------------------------- //

    // 사진 ID가 없으면 엔티티 대신 null이 나오는가?
    @Test
    fun `toLikedPhotoEntity - Photo(사진) ID가 없으면 엔티티를 만들지 않는다`() {
        // 실행 + 확인 : ID를 뺀 사진을 엔티티로 바꾸면 null 인가?
        assertNull(photo.copy(id = null).toLikedPhotoEntity())
    }

    // 만든 엔티티가 예상한 엔티티(이미지 경로는 null)와 같은가?
    @Test
    fun `toLikedPhotoEntity - Photo(사진)의 값을 엔티티에 그대로 옮긴다`() {
        // 실행 : 사진을 엔티티로 바꾼다 (좋아요 시각은 100 으로 고정)
        val result = photo.toLikedPhotoEntity(likedAt = 100L)

        // 확인 : 위에 만들어 둔 엔티티와 같은가? (이미지 파일은 아직 안 받았으니 경로는 null)
        assertEquals(entity.copy(localImagePath = null), result)
    }

    // 만든 사진의 값이 엔티티의 값(이미지 경로 포함)과 같은가?
    @Test
    fun `toPhoto - 엔티티의 값을 사진에 그대로 옮긴다`() {
        // 실행 : 엔티티를 사진으로 바꾼다
        val result = entity.toPhoto()

        // 확인 : 엔티티에 있던 값이 사진에 하나씩 그대로 들어갔는가?
        assertEquals("photo-1", result.id)
        assertEquals(4000, result.width)
        assertEquals(3000, result.height)
        assertEquals("#3C3C47", result.color)
        assertEquals("설명", result.description)
        assertEquals("부가설명", result.altDescription)
        assertEquals("small-url", result.urls?.small)
        assertEquals("regular-url", result.urls?.regular)
        assertEquals("정문기", result.user?.name)
        assertEquals("정문기", result.user?.username)
        assertEquals("profile-url", result.user?.profileImage?.medium)
        assertEquals("/files/unsplash_liked/photo-1.jpg", result.localImagePath)
    }

    // 만든 사진의 full·views·tags 가 null 인가? (0 이나 빈 값으로 채우면 오프라인 상세에 잘못 보임)
    @Test
    fun `toPhoto - 저장하지 않은 값은 채우지 않는다`() {
        // 실행 : 엔티티를 사진으로 바꾼다
        val result = entity.toPhoto()

        // 확인 : DB에 저장하지 않는 full · 조회수 · 태그가 비어 있는가?
        assertNull(result.urls?.full)
        assertNull(result.views)
        assertNull(result.tags)
    }

    // 다시 만든 사진이 원래 사진(저장하지 않은 값 제외)과 같은가?
    @Test
    fun `toLikedPhotoEntity + toPhoto - 엔티티로 바꿨다 되돌려도 저장한 값을 잃지 않는다`() {
        // 실행 : 사진 → 엔티티 → 사진 으로 바꿨다가 되돌린다
        val result = photo.toLikedPhotoEntity()?.toPhoto()

        // 확인 : 원래 사진에서 저장하지 않는 full · 조회수 · 태그만 빠지고 나머지는 같은가?
        assertEquals(photo.copy(urls = PhotoUrls(small = "small-url", regular = "regular-url"), views = null, tags = null), result)
    }
}