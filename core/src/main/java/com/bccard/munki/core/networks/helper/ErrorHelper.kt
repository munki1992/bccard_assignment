package com.bccard.munki.core.networks.helper

import com.bccard.munki.core.BuildConfig
import com.bccard.munki.core.consts.ErrorConstant
import com.bccard.munki.core.consts.NetworkStatus
import com.bccard.munki.core.extensions.transformJsonObjectToObject
import com.bccard.munki.core.networks.ApiError
import com.bccard.munki.core.networks.response.ErrorResponse
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Handle Error
 *
 * @author 정문기
 */

fun handleError(error: Throwable?): ApiError {
    if (error == null) return defaultError()

    return when (error) {
        is HttpException -> when (error.code()) {
            // 401 인증 만료/실패
            NetworkStatus.UNAUTHENTICATED       -> ApiError.SessionExpired

            // 404
            NetworkStatus.NOT_FOUND             -> defaultError("${error.message}")

            // 500/502
            NetworkStatus.BAD_GATEWAY,
            NetworkStatus.INTERNAL_SERVER_ERROR -> defaultError(ErrorConstant.COMMON_ERROR_MAINTENANCE)

            // 그 외 — 본문 JSON 을 ErrorResponse 로 파싱
            else -> runCatching {
                val body = error.response()?.errorBody()?.string()
                if (body.isNullOrBlank()) return@runCatching defaultError("${error.code()} ${error.message()}")

                body.transformJsonObjectToObject<ErrorResponse>()
                    ?.let {
                        ApiError.Server(
                            code        = it.code.takeUnless { c -> c == NetworkStatus.DEFAULT } ?: error.code(),
                            messageCode = it.messageCode,
                            message     = it.message ?: it.errors?.joinToString("\n")
                        )
                    }
                    ?: defaultError("${error.code()} ${error.message()}")
            }.getOrElse { handleError(it) }
        }

        // 인터넷 없음
        is UnknownHostException   -> ApiError.NoInternet

        // 타임아웃
        is SocketTimeoutException -> defaultError(ErrorConstant.COMMON_INTERNET_SLOWLY)

        // 그 외 예외 (메시지는 DEBUG 에서만 노출)
        else -> defaultError("${error.message}")
    }
}

// 기본 서버 오류 — 운영은 항상 점검 안내 문구, 디버그는 원인 메시지
fun defaultError(msg: String? = null, code: Int? = NetworkStatus.DEFAULT): ApiError.Server =
    ApiError.Server(
        code    = code,
        message = if (BuildConfig.DEBUG) msg ?: ErrorConstant.COMMON_ERROR_MAINTENANCE else ErrorConstant.COMMON_ERROR_MAINTENANCE
    )