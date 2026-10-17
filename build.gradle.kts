import org.gradle.api.tasks.testing.logging.TestLogEvent

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}

// 단위 테스트 실행 시 터미널에 테스트별 이름과 결과(통과 · 실패 · 건너뜀) 출력
//   LikedPhotoMapperTest > toLikedPhotoEntity - Photo(사진) ID가 없으면 엔티티를 만들지 않는다 PASSED
//   └──── ① 클래스 ────┘     └──────────────── ② 테스트 함수 이름 ──────────────────────┘  └③ 결과┘
subprojects {
    tasks.withType<Test>().configureEach {
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        }
    }
}