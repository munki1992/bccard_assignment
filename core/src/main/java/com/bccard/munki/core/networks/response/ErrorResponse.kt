package com.bccard.munki.core.networks.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Error Response
 *
 * @author 정문기
 */
@JsonClass(generateAdapter = true)
open class ErrorResponse(
    @param:Json(name = "code")
    var code: Int? = null,

    @param:Json(name = "success")
    var success: Boolean? = null,

    @param:Json(name = "data")
    var data: String? = null,

    @param:Json(name = "message")
    var message: String? = null,

    @param:Json(name = "messageCode")
    var messageCode: String? = null,

    @param:Json(name = "errors")
    var errors: List<String>? = null
)