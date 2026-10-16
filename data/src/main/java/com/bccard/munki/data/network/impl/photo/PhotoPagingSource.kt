package com.bccard.munki.data.network.impl.photo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bccard.munki.core.consts.Constants
import com.bccard.munki.data.network.service.photo.PhotoApi
import com.bccard.munki.domain.model.network.photo.Photo
import kotlin.coroutines.cancellation.CancellationException

/**
 * Photo Paging Source
 *
 * @author 정문기
 */
class PhotoPagingSource(
    private val api: PhotoApi
) : PagingSource<Int, Photo>() {

    // 이미 받은 사진 ID (중복 제거용)
    private val loadedIds = mutableSetOf<String>()

    // Photo List Load
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: Constants.SERVER_FIRST_PAGE

        return try {
            val photos = api.photoList(page = page, perPage = Constants.SERVER_LOAD_ITEMS_PER_PAGE)

            LoadResult.Page(
                data    = photos.filter { photo -> photo.id?.let(loadedIds::add) == true },
                prevKey = null,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // 새로고침은 항상 첫 페이지부터
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? = null
}