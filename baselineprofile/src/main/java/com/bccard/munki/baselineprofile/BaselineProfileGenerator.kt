package com.bccard.munki.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Baseline Profile Generator
 *
 * - 앱 시작 · 사진 목록 스크롤 · 좋아요 목록 이동 경로를 기록해 Baseline Profile 을 만든다
 * - 실행: ./gradlew :app:generateBaselineProfile (실기기 · Android 13 이상)
 * - 결과: app/src/release/generated/baselineProfiles/baseline-prof.txt
 *
 * @author 정문기
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(
        packageName = targetAppId,
        includeInStartupProfile = true,
        stableIterations = 3,
        maxIterations = 5
    ) {
        pressHome()
        startActivityAndWait()
        waitForPhotoList()
        scrollPhotoList()
        openFavoritesAndBack()
    }
}