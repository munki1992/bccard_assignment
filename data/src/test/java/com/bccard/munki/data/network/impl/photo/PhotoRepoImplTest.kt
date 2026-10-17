package com.bccard.munki.data.network.impl.photo

import android.content.Context
import com.bccard.munki.core.consts.NetworkStatus
import com.bccard.munki.core.extensions.isOnline
import com.bccard.munki.core.networks.ApiError
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.data.network.service.photo.PhotoApi
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoDownloadDTO
import com.bccard.munki.domain.model.network.photo.PhotoLinks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * Photo Repo Impl Test
 *
 * @author 정문기
 */
class PhotoRepoImplTest {

    // 부른 값을 기록하고, 정해 둔 에러를 순서대로 던지는 가짜 API
    private class FakePhotoApi(
        private val errors: MutableList<Exception> = mutableListOf()
    ) : PhotoApi {

        val requestedIds = mutableListOf<String>()
        val requestedUrls = mutableListOf<String>()

        override suspend fun photoList(page: Int, perPage: Int): List<Photo> = error("사용 안 함")

        override suspend fun photoDetail(id: String): Photo {
            requestedIds += id
            errors.removeFirstOrNull()?.let { throw it }
            return Photo(id = id)
        }

        override suspend fun trackDownload(url: String): PhotoDownloadDTO.Response {
            requestedUrls += url
            return PhotoDownloadDTO.Response(url = "original-url")
        }
    }

    // 인터넷 연결 확인에만 쓰는 가짜 Context
    private val context = mockk<Context>()

    // HTTP 에러 응답 만들기
    private fun httpError(code: Int) = HttpException(Response.error<Any>(code, "".toResponseBody()))

    // 인터넷 연결 상태 정하기 — isOnline() 은 확장 함수라 mockkStatic 으로 가짜를 만든다
    private fun online(isOnline: Boolean) = every { context.isOnline() } returns isOnline

    @Before
    fun setUp() {
        mockkStatic("com.bccard.munki.core.extensions.ContextExtKt")
        online(true)
    }

    @After
    fun tearDown() = unmockkAll()

    // Test ------------------------------------------------------------------------------------- //

    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // assertTrue   -> 조건이 true 면 통과, false 면 실패
    // every        -> 가짜 함수가 불리면 돌려줄 값을 정한다

    // Test ------------------------------------------------------------------------------------- //

    // API 가 사진을 주면 결과가 Success 이고, 그 안에 받은 사진이 들어 있는가?
    @Test
    fun `photoDetail - API 가 성공하면 사진을 담아 준다`() = runTest {
        // 준비 : 사진을 그대로 돌려주는 가짜 API
        val api = FakePhotoApi()

        // 실행 : photo-1 의 상세를 요청한다
        val result = PhotoRepoImpl(context, api).photoDetail("photo-1")

        // 확인 : 결과가 Success 이고, 사진 ID 가 photo-1 인가?
        assertEquals(ApiResult.Success(Photo(id = "photo-1")), result)
    }

    // 인터넷이 없으면 결과가 NoInternet 이고, API 는 한 번도 불리지 않았는가?
    @Test
    fun `photoDetail - 인터넷이 없으면 API 를 부르지 않고 NoInternet 을 준다`() = runTest {
        // 준비 : 인터넷이 끊긴 상태
        val api = FakePhotoApi()
        online(false)

        // 실행 : photo-1 의 상세를 요청한다
        val result = PhotoRepoImpl(context, api).photoDetail("photo-1")

        // 확인 : 결과가 NoInternet 이고, API 요청 기록이 비어 있는가?
        assertEquals(ApiResult.Failure(ApiError.NoInternet), result)
        assertTrue(api.requestedIds.isEmpty())
    }

    // 첫 요청이 401 이면 한 번 더 요청해서, 두 번째 결과를 받는가?
    @Test
    fun `photoDetail - 401 이면 한 번 더 요청한다`() = runTest {
        // 준비 : 첫 요청만 401 에러를 던지는 가짜 API
        val api = FakePhotoApi(errors = mutableListOf(httpError(NetworkStatus.UNAUTHENTICATED)))

        // 실행 : photo-1 의 상세를 요청한다
        val result = PhotoRepoImpl(context, api).photoDetail("photo-1")

        // 확인 : API 를 두 번 불렀고, 결과가 Success 인가?
        assertEquals(listOf("photo-1", "photo-1"), api.requestedIds)
        assertEquals(ApiResult.Success(Photo(id = "photo-1")), result)
    }

    // 서버 에러(500)가 나면 결과가 Failure 이고, 에러 종류가 Server 인가?
    @Test
    fun `photoDetail - 서버 에러가 나면 실패 결과를 준다`() = runTest {
        // 준비 : 500 에러를 던지는 가짜 API
        val api = FakePhotoApi(errors = mutableListOf(httpError(NetworkStatus.INTERNAL_SERVER_ERROR)))

        // 실행 : photo-1 의 상세를 요청한다
        val result = PhotoRepoImpl(context, api).photoDetail("photo-1")

        // 확인 : 결과가 Failure 이고, 그 안의 에러가 Server 에러인가?
        assertTrue(result is ApiResult.Failure)
        assertTrue((result as ApiResult.Failure).error is ApiError.Server)
    }

    // 사진에 download_location 이 있으면 그 주소 그대로 API 를 불렀는가?
    @Test
    fun `photoDownload - download_location 이 있으면 그 주소로 요청한다`() = runTest {
        // 준비 : download_location 이 있는 사진
        val api = FakePhotoApi()
        val photo = Photo(id = "photo-1", links = PhotoLinks(downloadLocation = "https://api.unsplash.com/photos/photo-1/download?ixid=abc"))

        // 실행 : 다운로드를 요청한다
        val result = PhotoRepoImpl(context, api).photoDownload(photo)

        // 확인 : download_location 주소로 불렀고, 원본 이미지 주소를 받았는가?
        assertEquals(listOf("https://api.unsplash.com/photos/photo-1/download?ixid=abc"), api.requestedUrls)
        assertEquals(ApiResult.Success(PhotoDownloadDTO.Response(url = "original-url")), result)
    }

    // 사진에 download_location 이 없으면 photos/{id}/download 주소로 API 를 불렀는가?
    @Test
    fun `photoDownload - download_location 이 없으면 사진 ID 로 주소를 만들어 요청한다`() = runTest {
        // 준비 : download_location 이 없는 사진
        val api = FakePhotoApi()
        val photo = Photo(id = "photo-1")

        // 실행 : 다운로드를 요청한다
        PhotoRepoImpl(context, api).photoDownload(photo)

        // 확인 : {id} 자리에 photo-1 이 들어간 주소로 불렀는가?
        assertEquals(listOf("photos/photo-1/download"), api.requestedUrls)
    }
}