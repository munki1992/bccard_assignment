package com.bccard.munki.core.networks

/**
 * Api Result
 *
 * @author 정문기
 */
sealed interface ApiResult<out T> {

    data class Success<T>(val data: T) : ApiResult<T>

    data class Failure(val error: ApiError) : ApiResult<Nothing> {
        val server: ApiError.Server? get() = error as? ApiError.Server
    }
}