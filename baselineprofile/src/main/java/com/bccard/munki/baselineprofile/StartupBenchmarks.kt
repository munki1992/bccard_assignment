package com.bccard.munki.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Startup Benchmarks
 *
 * - Baseline Profile 적용 전후의 앱 시작 시간(Cold Start)을 비교한다
 * - 실행: ./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest (실기기)
 *
 * @author 정문기
 */
@RunWith(AndroidJUnit4::class)
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    // 프로파일 없이 (설치 직후 상태)
    @Test
    fun startupCompilationNone() = benchmark(CompilationMode.None())

    // Baseline Profile 적용
    @Test
    fun startupCompilationBaselineProfiles() = benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) = rule.measureRepeated(
        packageName = targetAppId,
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        startupMode = StartupMode.COLD,
        iterations = 5,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
        waitForPhotoList()
    }
}