package com.bccard.munki.data.network.service.photo

import com.bccard.munki.core.consts.Constants
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoDetailDTO
import com.bccard.munki.domain.model.network.photo.PhotoListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Photo Api
 *
 *  - Unsplash 응답 본문 타입을 그대로 받기!
 *
 * @author 정문기
 */
interface PhotoApi {

    // 사진 목록 — page 는 1부터
    @GET(PhotoListDTO.URL)
    suspend fun photoList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = Constants.SERVER_LOAD_ITEMS_PER_PAGE
    ): List<Photo>

    // 사진 상세
    @GET(PhotoDetailDTO.URL)
    suspend fun photoDetail(@Path("id") id: String): Photo
}