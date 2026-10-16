package com.bccard.munki.core.consts

/**
 * Http Const
 *
 * @author 정문기
 */
object HttpConst {
    const val REST_AUTHORIZATION        = "Authorization"
    const val REST_BEARER               = "Bearer"
    const val REST_CLIENT_ID            = "Client-ID"
    const val REST_NAME_X_REQUESTED     = "X-Requested-With"
    const val REST_NAME_CONTENT_TYPE    = "Content-Type"
    const val REST_VALUE_XML_REQUEST    = "XMLHttpRequest"
    const val REST_VALUE_JSON           = "application/json"
    const val REST_USER_AGENT           = "User-Agent"
    const val REST_HTTP_AGENT           = "http.agent"

    // 커스텀 헤더
    const val X_PLATFORM                = "X-Platform"
    const val X_APP_VERSION             = "X-App-Version"
    const val X_BUILD_NUMBER            = "X-Build-Number"
    const val X_OS_VERSION              = "X-OS-Version"
    const val X_SDK_VERSION             = "X-SDK-Version"
    const val X_DEVICE_MODEL            = "X-Device-Model"
    const val ACCEPT_LANGUAGE           = "Accept-Language"
}