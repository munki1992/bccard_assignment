package com.bccard.munki.fake

import androidx.paging.PagingData
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoDownloadDTO
import com.bccard.munki.domain.repository.network.photo.PhotoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake Photo Repo
 *
 * API 대신 테스트에서 정해 둔 결과를 돌려주는 가짜 PhotoRepo
 *
 * @author 정문기
 */
class FakePhotoRepo(
    private val detailResult: ApiResult<Photo> = ApiResult.Success(Photo()),
    private val downloadResult: ApiResult<PhotoDownloadDTO.Response> = ApiResult.Success(PhotoDownloadDTO.Response())
) : PhotoRepo {

    override fun photoPagingData(): Flow<PagingData<Photo>> = flowOf(PagingData.empty())

    override suspend fun photoDetail(id: String): ApiResult<Photo> = detailResult

    override suspend fun photoDownload(photo: Photo): ApiResult<PhotoDownloadDTO.Response> = downloadResult
}