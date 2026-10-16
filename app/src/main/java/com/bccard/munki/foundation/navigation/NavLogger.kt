package com.bccard.munki.foundation.navigation

import android.os.Bundle
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.bccard.munki.BuildConfig
import timber.log.Timber

/**
 * Nav Logger
 *
 * TODO Crashlytics (breadcrumb / recordException) — Firebase 를 붙일 때 breadcrumb 에 연결
 *
 * @author 정문기
 */
class NavLogger(private val host: String) {

    companion object {
        const val HOST_APP  = "app"

        private const val TAG  = "NAV_LOG"
        private const val LINE = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    }

    // 이동 요청
    fun request(nav: NavHostController, action: String, name: String, path: String? = null, options: String? = null) {
        val from = nav.currentDestination?.route ?: "(없음)"
        breadcrumb("$action: $from → $name")

        debugLog {
            listOfNotNull(
                LINE,
                "┌ [$host 요청] ▶ $action",
                "├ [현재 위치] ▶ $from",
                "├ [이동 목표] ▶ $name${path?.let { " → $it" }.orEmpty()}",
                options?.let { "├ [옵션] ▶ $it" }
            )
        }
    }

    // 이동 요청 결과
    fun result(success: Boolean, reason: String? = null) {
        if (!success) breadcrumb("blocked: $reason")

        debugLog { listOf(if (success) "└ [완료] ▶ 이동 요청 전달" else "└ [차단] ▶ $reason", LINE) }
    }

    // 예외 방어
    fun error(action: String, target: String, e: Throwable) {
        breadcrumb("error: $action → $target (${e::class.simpleName})")

        debugLog(e) { listOf("└ [에러] ▶ $action → $target: ${e.message}", LINE) }
    }

    // 실제 도착
    fun arrive(destination: NavDestination, args: Bundle?) {
        val pattern = destination.route ?: "(route 없음, id=${destination.id})"
        breadcrumb("arrive: $pattern")

        debugLog {
            val argsText = args
                ?.let { bundle -> destination.arguments.entries.joinToString("") { (key, arg) -> "\n    ➔ $key : ${arg.type[bundle, key]}" } }
                .takeUnless { it.isNullOrEmpty() }
                ?: " 없음"

            listOf(LINE, "▶ [$host 도착] ▶ $pattern", "├ [전달 데이터] ▶ $argsText", LINE)
        }
    }

    // 디버그 빌드에서만 줄마다 출력
    private inline fun debugLog(throwable: Throwable? = null, lines: () -> List<String>) {
        if (!BuildConfig.DEBUG) return

        lines().forEachIndexed { index, line -> Timber.tag(TAG).e(throwable.takeIf { index == 0 }, line) }
    }

    // Crashlytics 브레드크럼 — TODO Firebase 연결 전까지
    @Suppress("UNUSED_PARAMETER")
    private fun breadcrumb(message: String) { }
}