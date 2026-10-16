package com.bccard.munki.foundation.navigation.app

import androidx.navigation.NavBackStackEntry
import kotlin.reflect.KClass

/**
 * App Navigator
 *
 * @author 정문기
 */
interface AppNavigator {
    fun push(route: AppRoute)                                                                       // 이동 (route 에 정의된 navOptions 적용)
    fun <T> pushWithData(route: AppRoute, key: String, data: T)                                     // 데이터와 함께 이동

    fun pop()                                                                                       // 뒤로가기
    fun popTo(route: KClass<out AppRoute>, inclusive: Boolean = false)                              // 특정 화면까지 뒤로가기 (inclusive 면 그 화면도 제거) — popTo<라우트>() 로 호출
    fun <T> popWithResult(key: String, value: T)                                                    // 결과와 함께 뒤로가기

    fun graphEntry(route: KClass<out AppRoute>): NavBackStackEntry                                  // 중첩 그래프 엔트리 (그래프 스코프 ViewModel 공유용) — graphEntry<라우트>() 로 호출
}

inline fun <reified T : AppRoute> AppNavigator.popTo(inclusive: Boolean = false) = popTo(T::class, inclusive)
inline fun <reified T : AppRoute> AppNavigator.graphEntry(): NavBackStackEntry = graphEntry(T::class)