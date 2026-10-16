package com.bccard.munki.domain.model.network.photo

import com.squareup.moshi.JsonClass

/**
 * PhotoDownloadDTO
 *
 * 사진 다운로드
 *
 * @author 정문기
 */
object PhotoDownloadDTO {

    const val URL = "photos/{id}/download"

    @JsonClass(generateAdapter = true)
    data class Response(
        val url: String? = null                                                                     // 원본 이미지 다운로드 URL
    )
}