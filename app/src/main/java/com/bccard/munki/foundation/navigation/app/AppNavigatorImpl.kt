package com.bccard.munki.foundation.navigation.app

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.bccard.munki.foundation.navigation.NavLogger
import kotlin.reflect.KClass

/**
 * App Navigator Impl
 *
 * @author 정문기
 */
class AppNavigatorImpl(private val nav: NavHostController) : AppNavigator {

    private val log = NavLogger(NavLogger.HOST_APP)

    // 뒤로가기 계열 로그용 — 백스택의 이전 화면 route
    private val previousRoute
        get() = nav.previousBackStackEntry?.destination?.route ?: "(이전 화면 없음)"

    override fun push(route: AppRoute) {
        navigate(route)
    }

    override fun <T> pushWithData(route: AppRoute, key: String, data: T) {
        // 이동에 성공했을 때만 다음 화면의 SavedStateHandle 에 전달 — 실패하면 currentBackStackEntry 가 아직 현재 화면이다
        if (navigate(route)) nav.currentBackStackEntry?.savedStateHandle?.set(key, data)
    }

    override fun pop() = track("pop", previousRoute, reason = "백스택에 이전 화면 없음") { nav.popBackStack() }

    override fun popTo(route: KClass<out AppRoute>, inclusive: Boolean) = route.simpleName.orEmpty().let { name ->
        track("popTo", name, "inclusive=$inclusive", "백스택에 $name 없음") { nav.popBackStack(route, inclusive) }
    }

    override fun <T> popWithResult(key: String, value: T) = track("popWithResult", previousRoute, "key=$key", "이전 화면 없음") {
        // 이전 화면의 SavedStateHandle 에 전달
        nav.previousBackStackEntry?.savedStateHandle?.set(key, value)
        nav.navigateUp()
    }

    // 해당 그래프 안에 있을 때만 호출 (없으면 예외)
    override fun graphEntry(route: KClass<out AppRoute>): NavBackStackEntry = nav.getBackStackEntry(route)

    // push 계열 공통 — 이동 성공 여부를 돌려준다 (navigate 예외는 앱이 죽지 않게 삼키고 로그)
    private fun navigate(route: AppRoute): Boolean {
        log.request(
            nav     = nav,
            action  = "push",
            name    = route::class.simpleName.orEmpty(),
            path    = route.toString(),
            options = "clearBackStack=${route.clearBackStack}, singleTop=${route.navOptions != null}"
        )

        return runCatching {
            nav.navigate(route) {
                // 그래프는 남기고 시작 목적지까지 제거 → 전환 애니메이션 유지
                if (route.clearBackStack) popUpTo(nav.graph.findStartDestination().id) { inclusive = true }

                route.navOptions?.invoke(this)
            }
        }.fold(
            onSuccess = { log.result(success = true); true },
            onFailure = { log.error("push", route.toString(), it); false }
        )
    }

    // 뒤로가기 계열 공통 — 요청 로그 → 실행 → 결과 로그 (false 면 reason 으로 차단 표시)
    private inline fun track(action: String, name: String, options: String? = null, reason: String, block: () -> Boolean) {
        log.request(nav, action, name, options = options)
        log.result(block(), reason)
    }
}