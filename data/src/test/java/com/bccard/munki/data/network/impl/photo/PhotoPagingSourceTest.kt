package com.bccard.munki.data.network.impl.photo

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.bccard.munki.core.consts.Constants
import com.bccard.munki.data.network.service.photo.PhotoApi
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoDownloadDTO
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Photo Paging Source Test
 *
 * @author 정문기
 */
class PhotoPagingSourceTest {

    // 페이지 번호별로 정해 둔 사진을 돌려주는 가짜 API (요청한 페이지 번호를 기록)
    private class FakePhotoApi(
        private val pages: Map<Int, List<Photo>> = emptyMap(),
        private val error: Exception? = null
    ) : PhotoApi {

        val requestedPages = mutableListOf<Int>()
        val requestedPerPages = mutableListOf<Int>()

        override suspend fun photoList(page: Int, perPage: Int): List<Photo> {
            requestedPages += page
            requestedPerPages += perPage
            error?.let { throw it }
            return pages[page].orEmpty()
        }

        override suspend fun photoDetail(id: String): Photo = error("사용 안 함")

        override suspend fun trackDownload(url: String): PhotoDownloadDTO.Response = error("사용 안 함")
    }

    // 사진 ID 목록으로 사진 만들기
    private fun photos(vararg ids: String) = ids.map { Photo(id = it) }

    // 처음 불러오기 (key 없음)
    private fun refresh() = LoadParams.Refresh<Int>(key = null, loadSize = Constants.SERVER_LOAD_ITEMS_PER_PAGE, placeholdersEnabled = false)

    // 다음 페이지 불러오기
    private fun append(page: Int) = LoadParams.Append(key = page, loadSize = Constants.SERVER_LOAD_ITEMS_PER_PAGE, placeholdersEnabled = false)

    // Test ------------------------------------------------------------------------------------- //

    // assertNull   -> 값이 null 이면 통과, 아니면 실패
    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // assertTrue   -> 조건이 true 면 통과, false 면 실패

    // Test ------------------------------------------------------------------------------------- //

    // 처음 불러올 때 요청한 페이지 번호가 1 이고, 페이지당 개수가 20 인가?
    @Test
    fun `load - 처음 불러올 때 1페이지부터 요청한다`() = runTest {
        // 준비 : 1페이지에 사진 a, b 를 주는 가짜 API
        val api = FakePhotoApi(pages = mapOf(1 to photos("a", "b")))

        // 실행 : 목록을 처음 불러온다
        PhotoPagingSource(api).load(refresh())

        // 확인 : API 에 1페이지, 20개로 요청했는가?
        assertEquals(listOf(Constants.SERVER_FIRST_PAGE), api.requestedPages)
        assertEquals(listOf(Constants.SERVER_LOAD_ITEMS_PER_PAGE), api.requestedPerPages)
    }

    // 받은 결과의 사진이 API 가 준 사진과 같고, 다음 페이지 번호가 2 · 이전 페이지 번호가 null 인가?
    @Test
    fun `load - 받은 사진을 돌려주고 다음 페이지 번호를 1 올린다`() = runTest {
        // 준비 : 1페이지에 사진 a, b 를 주는 가짜 API
        val api = FakePhotoApi(pages = mapOf(1 to photos("a", "b")))

        // 실행 : 목록을 처음 불러온다
        val result = PhotoPagingSource(api).load(refresh()) as LoadResult.Page

        // 확인 : 사진 a, b 가 왔고, 다음은 2페이지 · 이전 페이지는 없는가?
        assertEquals(photos("a", "b"), result.data)
        assertEquals(2, result.nextKey)
        assertNull(result.prevKey)
    }

    // 빈 페이지가 오면 다음 페이지 번호가 null 인가? (null 이면 스크롤 끝)
    @Test
    fun `load - 빈 페이지가 오면 더 불러오지 않는다`() = runTest {
        // 준비 : 3페이지에 사진이 하나도 없는 가짜 API
        val api = FakePhotoApi(pages = mapOf(3 to emptyList()))

        // 실행 : 스크롤해서 3페이지를 불러온다
        val result = PhotoPagingSource(api).load(append(3)) as LoadResult.Page

        // 확인 : 받은 사진이 없고, 다음 페이지가 없는가?
        assertTrue(result.data.isEmpty())
        assertNull(result.nextKey)
    }

    // 1페이지에서 받은 사진 ID 가 2페이지에 또 오면 결과에서 빠지는가?
    @Test
    fun `load - 이미 받은 사진은 다시 넣지 않는다`() = runTest {
        // 준비 : 1페이지에 a, b / 2페이지에 b, c 를 주는 가짜 API (b 가 겹침)
        val api = FakePhotoApi(pages = mapOf(1 to photos("a", "b"), 2 to photos("b", "c")))
        val source = PhotoPagingSource(api)

        // 실행 : 1페이지를 불러온 뒤, 스크롤해서 2페이지를 불러온다
        source.load(refresh())
        val result = source.load(append(2)) as LoadResult.Page

        // 확인 : 2페이지 결과에 겹친 b 는 빠지고 c 만 있고, 다음은 3페이지인가?
        assertEquals(photos("c"), result.data)
        assertEquals(3, result.nextKey)
    }

    // API 에서 에러가 나면 결과가 LoadResult.Error 이고, 같은 에러가 들어 있는가? (Error 면 목록에 다시 시도 버튼이 보임)
    @Test
    fun `load - API 에러가 나면 에러 결과를 돌려준다`() = runTest {
        // 준비 : 부르면 서버 에러를 던지는 가짜 API
        val exception = IllegalStateException("서버 에러")
        val api = FakePhotoApi(error = exception)

        // 실행 : 목록을 처음 불러온다
        val result = PhotoPagingSource(api).load(refresh())

        // 확인 : 결과가 에러이고, 그 안에 API 가 던진 에러가 들어 있는가?
        assertTrue(result is LoadResult.Error)
        assertEquals(exception, (result as LoadResult.Error).throwable)
    }
}