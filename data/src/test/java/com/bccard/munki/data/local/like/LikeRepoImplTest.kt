package com.bccard.munki.data.local.like

import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoUrls
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Like Repo Impl Test
 *
 * @author 정문기
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LikeRepoImplTest {

    // Room 대신 메모리(Map)에 저장하는 가짜 DAO
    private class FakeLikedPhotoDao : LikedPhotoDao {

        val rows = MutableStateFlow<Map<String, LikedPhotoEntity>>(emptyMap())

        override fun observeLikedPhotos(): Flow<List<LikedPhotoEntity>> = rows.map { it.values.sortedByDescending { entity -> entity.likedAt } }

        override fun observeLikedIds(): Flow<List<String>> = rows.map { it.keys.toList() }

        override fun observeIsLiked(id: String): Flow<Boolean> = rows.map { id in it }

        override suspend fun insertLikedPhoto(photo: LikedPhotoEntity) = rows.update { it + (photo.id to photo) }

        override suspend fun deleteLikedPhoto(id: String) = rows.update { it - id }

        override suspend fun getLikedPhoto(id: String): LikedPhotoEntity? = rows.value[id]

        override suspend fun updateImagePath(id: String, path: String?) = rows.update { map ->
            map[id]?.let { map + (id to it.copy(localImagePath = path)) } ?: map
        }
    }

    // API 에서 받은 사진
    private val photo = Photo(id = "photo-1", urls = PhotoUrls(regular = "regular-url"))

    // 저장한 이미지 파일 경로
    private val imagePath = "/files/unsplash_liked/photo-1.jpg"

    private val dao = FakeLikedPhotoDao()

    // 이미지 파일 보관함 (가짜) — 받기 성공이면 경로, 실패면 null 을 돌려주게 테스트마다 정한다
    private val imageStore = mockk<LikedImageStore>(relaxed = true)

    // 이미지 저장을 테스트 스코프에서 돌려서 runCurrent() 로 끝날 때까지 기다릴 수 있게 한다
    private fun TestScope.repo() = LikeRepoImpl(dao, imageStore, backgroundScope)

    // Test ------------------------------------------------------------------------------------- //

    // assertNull   -> 값이 null 이면 통과, 아니면 실패
    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // assertTrue   -> 조건이 true 면 통과, false 면 실패
    // coEvery      -> 가짜 함수가 불리면 돌려줄 값을 정한다
    // coVerify     -> 함수가 (그 값으로) 불렸으면 통과, 아니면 실패

    // Test ------------------------------------------------------------------------------------- //

    // 좋아요하지 않은 사진을 토글하면 DB 에 사진 ID 가 들어가는가?
    @Test
    fun `toggle - 좋아요하지 않은 사진이면 좋아요 목록에 추가한다`() = runTest {
        // 실행 : 하트를 누른다 (DB가 비어 있으니 좋아요 추가)
        repo().toggle(photo)

        // 확인 : DB에 photo-1 하나만 들어 있는가?
        assertEquals(setOf("photo-1"), dao.rows.value.keys)
    }

    // 좋아요한 사진을 토글하면 DB 가 비고, 저장해 둔 이미지 파일 삭제가 불렸는가?
    @Test
    fun `toggle - 좋아요한 사진이면 좋아요 목록에서 빼고 이미지 파일도 지운다`() = runTest {
        // 준비 : 이미지 파일까지 저장된 좋아요 사진을 DB에 미리 넣어 둔다
        dao.insertLikedPhoto(photo.toLikedPhotoEntity()!!.copy(localImagePath = imagePath))

        // 실행 : 하트를 누른다 (이미 좋아요라서 좋아요 취소)
        repo().toggle(photo)

        // 확인 : DB가 비었고, 저장해 둔 이미지 파일 삭제가 불렸는가?
        assertTrue(dao.rows.value.isEmpty())
        coVerify { imageStore.delete(imagePath) }
    }

    // 사진 ID가 없으면 토글해도 DB가 비어 있는가?
    @Test
    fun `toggle - 사진 ID가 없으면 좋아요 목록에 추가하지 않는다`() = runTest {
        // 실행 : ID가 없는 사진으로 하트를 누른다
        repo().toggle(photo.copy(id = null))

        // 확인 : DB가 그대로 비어 있는가?
        assertTrue(dao.rows.value.isEmpty())
    }

    // 이미지를 다 받은 뒤 DB의 이미지 경로가 받은 파일 경로인가?
    @Test
    fun `toggle - 이미지를 받으면 파일 경로를 기록한다`() = runTest {
        // 준비 : 이미지 받기가 성공해서 파일 경로를 돌려주게 한다
        coEvery { imageStore.save("photo-1", "regular-url") } returns imagePath

        // 실행 : 하트를 누르고, 뒤에서 도는 이미지 받기까지 끝낸다
        repo().toggle(photo)
        runCurrent()

        // 확인 : DB 에 받은 파일 경로가 기록됐는가?
        assertEquals(imagePath, dao.rows.value["photo-1"]?.localImagePath)
    }

    // 이미지를 못 받으면 사진은 저장되고 이미지 경로는 null 인가?
    @Test
    fun `toggle - 이미지를 못 받아도 좋아요 목록에는 추가한다`() = runTest {
        // 준비 : 이미지 받기가 실패해서 null을 돌려주게 한다
        coEvery { imageStore.save(any(), any()) } returns null

        // 실행 : 하트를 누르고, 뒤에서 도는 이미지 받기까지 끝낸다
        repo().toggle(photo)
        runCurrent()

        // 확인 : DB 에 photo-1 은 들어 있고, 이미지 경로는 비어 있는가?
        assertEquals(setOf("photo-1"), dao.rows.value.keys)
        assertNull(dao.rows.value["photo-1"]?.localImagePath)
    }

    // 이미지를 받는 동안 좋아요를 끄면 DB가 비고, 다 받은 파일 삭제가 불렸는가?
    @Test
    fun `toggle - 이미지를 받는 동안 좋아요를 끄면 받은 파일을 지운다`() = runTest {
        // 준비 : 이미지 받기가 성공해서 파일 경로를 돌려주게 한다
        coEvery { imageStore.save("photo-1", "regular-url") } returns imagePath
        val repo = repo()

        // 실행 : 하트를 누르고, 이미지를 다 받기 전에 한 번 더 눌러 끈 뒤, 이미지 받기를 끝낸다
        repo.toggle(photo)
        repo.toggle(photo)
        runCurrent()

        // 확인 : DB가 비었고, 뒤늦게 받은 이미지 파일 삭제가 불렸는가?
        assertTrue(dao.rows.value.isEmpty())
        coVerify { imageStore.delete(imagePath) }
    }

    // 좋아요한 사진 ID 목록이 DB에 저장된 ID와 같은가?
    @Test
    fun `likedIds - 좋아요한 사진 ID를 모두 준다`() = runTest {
        val repo = repo()

        // 준비 : photo-1, photo-2 두 장에 하트를 누른다
        repo.toggle(photo)
        repo.toggle(photo.copy(id = "photo-2"))

        // 확인 : 좋아요 ID 목록이 photo-1, photo-2 인가?
        assertEquals(setOf("photo-1", "photo-2"), repo.likedIds.first())
    }

    // 좋아요하지 않은 사진 ID로 찾으면 null 이 나오는가?
    @Test
    fun `findLikedPhoto - 좋아요하지 않은 사진이면 null 을 준다`() = runTest {
        // 실행 + 확인 : 비어 있는 DB에서 photo-1 을 찾으면 null 인가?
        assertNull(repo().findLikedPhoto("photo-1"))
    }
}