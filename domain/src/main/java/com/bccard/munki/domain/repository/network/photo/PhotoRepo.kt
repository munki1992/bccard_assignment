package com.bccard.munki.domain.repository.network.photo

import androidx.paging.PagingData
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.domain.model.network.photo.Photo
import kotlinx.coroutines.flow.Flow

/**
 * Photo Repo
 *
 * @author 정문기
 */
interface PhotoRepo {

    // 사진 목록 (페이지 단위)
    fun photoPagingData(): Flow<PagingData<Photo>>

    // 사진 상세
    suspend fun photoDetail(id: String): ApiResult<Photo>
}