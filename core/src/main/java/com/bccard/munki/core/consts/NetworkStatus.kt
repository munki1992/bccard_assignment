package com.bccard.munki.core.consts

/**
 * Network Status
 *
 * @author 정문기
 */
object NetworkStatus {
    const val DEFAULT               = 0                                                             // 상태 없음 — 인터넷 미연결·타임아웃 등 응답 자체가 없는 경우
    const val BAD_REQUEST           = 400                                                           // 잘못된 요청 — 입력값 오류 (messageCode 로 세부 분기)
    const val UNAUTHENTICATED       = 401                                                           // 인증 실패 — 토큰 만료 → 세션 만료 처리
    const val NOT_FOUND             = 404                                                           // 없는 API·리소스
    const val INTERNAL_SERVER_ERROR = 500                                                           // 서버 내부 오류
    const val BAD_GATEWAY           = 502                                                           // 게이트웨이 오류 — 서버 앞단(프록시)이 응답을 못 받음
    const val SERVICE_UNAVAILABLE   = 503                                                           // 서비스 이용불가 — 점검·과부하
}