package com.bccard.munki.data.network.impl.photo

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bccard.munki.core.consts.Constants
import com.bccard.munki.data.network.impl.BaseRepoImpl
import com.bccard.munki.data.network.service.photo.PhotoApi
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoDownloadDTO
import com.bccard.munki.domain.repository.network.photo.PhotoRepo
import kotlinx.coroutines.flow.Flow

/**
 * Photo Repo Impl
 *
 * @author 정문기
 */
class PhotoRepoImpl(
    context: Context,
    private val api: PhotoApi
) : BaseRepoImpl(context), PhotoRepo {

    // 사진 목록 (페이지 단위)
    override fun photoPagingData(): Flow<PagingData<Photo>> = Pager(
        config = PagingConfig(
            pageSize           = Constants.SERVER_LOAD_ITEMS_PER_PAGE,                              // 한 번에 불러올 개수
            initialLoadSize    = Constants.SERVER_LOAD_ITEMS_PER_PAGE,                              // 첫 요청 개수
            prefetchDistance   = Constants.LOAD_MORE_THRESHOLD,                                     // 끝에서 몇 칸 남으면 다음 페이지 요청
            enablePlaceholders = false                                                              // 아직 안 온 칸을 빈 자리로 미리 그릴지
        ),
        pagingSourceFactory = { PhotoPagingSource(api) }                                            // 새로고침마다 새 PagingSource
    ).flow

    // 사진 상세
    override suspend fun photoDetail(id: String) = request { api.photoDetail(id) }

    // 다운로드 추적 (다운로드 전 필수 호출)
    override suspend fun photoDownload(photo: Photo) = request {
        api.trackDownload(photo.links?.downloadLocation ?: PhotoDownloadDTO.URL.replace("{id}", photo.id.orEmpty()))
    }
}