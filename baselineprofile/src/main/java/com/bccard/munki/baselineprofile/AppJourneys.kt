package com.bccard.munki.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

/**
 * App Journeys
 *
 * Baseline Profile 생성 · 시작 성능 측정이 같이 쓰는 사용자 동작
 *
 * @author 정문기
 */

// 측정 대상 앱 패키지 (baselineprofile/build.gradle.kts 의 targetAppId)
val targetAppId: String
    get() = InstrumentationRegistry.getArguments().getString("targetAppId")
        ?: error("targetAppId 가 전달되지 않았습니다")

private const val TIMEOUT = 5_000L

// 사진 목록이 나올 때까지 대기 — 상단 하트(좋아요 목록) 버튼으로 첫 화면을 확인
fun MacrobenchmarkScope.waitForPhotoList() {
    device.wait(Until.hasObject(By.desc("좋아요 목록")), TIMEOUT)
    device.wait(Until.hasObject(By.scrollable(true)), TIMEOUT)
}

// 사진 목록 스크롤 — 아래로 내리며 다음 페이지 로드 · 카드 그리기 경로를 기록
fun MacrobenchmarkScope.scrollPhotoList() {
    val list = device.findObject(By.scrollable(true)) ?: return
    list.setGestureMargin(device.displayWidth / 5)
    repeat(3) {
        list.fling(Direction.DOWN)
        device.waitForIdle()
    }
    list.fling(Direction.UP)
    device.waitForIdle()
}

// 좋아요 목록 화면으로 이동했다가 뒤로 돌아오기
fun MacrobenchmarkScope.openFavoritesAndBack() {
    device.findObject(By.desc("좋아요 목록"))?.click() ?: return
    device.wait(Until.hasObject(By.desc("뒤로가기")), TIMEOUT)
    device.pressBack()
    waitForPhotoList()
}