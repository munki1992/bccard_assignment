package com.bccard.munki.data.network.impl

import android.content.Context
import com.bccard.munki.core.consts.NetworkStatus
import com.bccard.munki.core.extensions.isOnline
import com.bccard.munki.core.networks.ApiError
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.core.networks.helper.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * Base Repo Impl
 *
 * @author 정문기
 */
abstract class BaseRepoImpl(
    private val context: Context
) {

    // 응답 본문을 그대로 결과로 돌려준다
    protected suspend fun <T : Any> request(call: suspend () -> T): ApiResult<T> =
        guard { ApiResult.Success(callWithRetry(call)) }

    // 공통 전처리 + 예외 매핑
    private suspend fun <T : Any> guard(block: suspend () -> ApiResult<T>): ApiResult<T> = withContext(Dispatchers.IO) {
        if (!context.isOnline())
            return@withContext ApiResult.Failure(ApiError.NoInternet)

        runCatching {
            block()
        }.getOrElse { ApiResult.Failure(handleError(it)) }
    }

    // 401 이면 한 번 더 (토큰 재발급 후 재시도)
    private suspend fun <R> callWithRetry(call: suspend () -> R): R =
        try {
            call()
        } catch (e: HttpException) {
            if (e.code() == NetworkStatus.UNAUTHENTICATED)
                call()
            else throw e
        }
}