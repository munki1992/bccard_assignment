package com.bccard.munki.core.networks

/**
 * Api Error
 *
 * @author 정문기
 */
sealed interface ApiError {
    data object SessionExpired : ApiError                                                           // 401 — 로컬 인증 데이터 삭제 후 처음부터
    data object NoInternet     : ApiError                                                           // 오프라인

    data class Server(                                                                              // 서버 오류 응답(4xx/5xx 본문) 또는 예외 메시지
        val code: Int? = null,                                                                      // HTTP 상태 코드 (예외는 NetworkStatus.DEFAULT)
        val messageCode: String? = null,                                                            // 서버 messageCode
        val message: String? = null                                                                 // 사용자에게 보여줄 메시지
    ) : ApiError
}