package com.bccard.munki

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Main Dispatcher Rule
 *
 * ViewModel 의 viewModelScope 는 Main(안드로이드 화면 스레드)에서 돈다
 *
 * PC 테스트에는 Main 이 없어서 테스트용으로 바꿔 끼운다
 *
 * @author 정문기
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    // 테스트 시작 전 — Main 을 테스트용으로 교체
    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)

    // 테스트 끝난 뒤 — 원래대로
    override fun finished(description: Description) = Dispatchers.resetMain()
}