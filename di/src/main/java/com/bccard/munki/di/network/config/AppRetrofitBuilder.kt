package com.bccard.munki.di.network.config

import com.bccard.munki.core.BuildConfig
import com.bccard.munki.core.common.DeviceInfo
import com.bccard.munki.core.consts.Constants
import com.bccard.munki.core.consts.HttpConst
import com.bccard.munki.core.networks.retrofit.BaseRetrofitBuilder
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import java.util.Locale

/**
 * App Retrofit Builder
 *
 * @author 정문기
 */
class AppRetrofitBuilder(
    baseUrl: String
) : BaseRetrofitBuilder(baseUrl) {

    override fun createRetrofit(): Retrofit {
        addHeaderInterceptor(HeaderInterceptor())
        return retrofit
    }

    // 헤더 추가 (모든 요청에 반응)
    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()

            // Unsplash 공개 API 인증 — Authorization: Client-ID {Access Key}
            Constants.UNSPLASH_ACCESS_KEY.takeIf { it.isNotBlank() }?.let {
                builder.addHeader(HttpConst.REST_AUTHORIZATION, "${HttpConst.REST_CLIENT_ID} $it")
            }

            // 기본 헤더 설정
            val headers = mapOf(
                HttpConst.REST_NAME_X_REQUESTED  to HttpConst.REST_VALUE_XML_REQUEST,               // AJAX 요청 표시
                HttpConst.REST_NAME_CONTENT_TYPE to HttpConst.REST_VALUE_JSON,                      // 컨텐츠 타입 (JSON)
                HttpConst.REST_USER_AGENT        to System.getProperty(HttpConst.REST_HTTP_AGENT),  // 유저 에이전트
                HttpConst.X_PLATFORM             to "Android",                                      // 플랫폼
                HttpConst.X_APP_VERSION          to BuildConfig.VERSION_NAME,                       // 앱 버전 (1.0.0)
                HttpConst.X_BUILD_NUMBER         to BuildConfig.VERSION_CODE,                       // 빌드 번호 (1)
                HttpConst.X_OS_VERSION           to DeviceInfo.getOSVersion(),                      // OS 버전 (Android 16)
                HttpConst.X_SDK_VERSION          to DeviceInfo.getSDKVersion().toString(),          // SDK 버전 (36)
                HttpConst.X_DEVICE_MODEL         to DeviceInfo.getDeviceModel(),                    // 기기 정보 (SM-F936N)
                HttpConst.ACCEPT_LANGUAGE        to Locale.getDefault().toLanguageTag()             // 언어 (ko-KR)
            )

            headers.forEach { (key, value) ->
                value?.let { builder.addHeader(key, it) }
            }

            return chain.proceed(builder.build())
        }
    }

    // 토큰 갱신 (401이 올때만 반응) — 아직 createRetrofit 에 연결하지 않는다
    inner class TokenAuthenticator : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            // 무한루프 방지 (최대 2회 재시도)
            if (responseCount(response) >= 2) return null

            // TODO 토큰 갱신 — 지금은 갱신하지 않고 401 을 그대로 돌려준다
            return null
        }

        private fun responseCount(response: Response): Int {
            var count = 1
            var prior = response.priorResponse
            while (prior != null) {
                count++
                prior = prior.priorResponse
            }
            return count
        }
    }
}