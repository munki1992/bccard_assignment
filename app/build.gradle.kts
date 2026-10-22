import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
//    alias(libs.plugins.google.services)
//    alias(libs.plugins.firebase.crashlytics)
//    alias(libs.plugins.baselineprofile)
}

// 릴리스 서명키 정보
val keystoreProperties = Properties().apply {
    providers.fileContents(rootProject.layout.projectDirectory.file("keystore/keystore.properties")).asText.orNull?.let { load(it.reader()) }
}

android {
    namespace = "com.bccard.munki"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        applicationId = "com.bccard.munki"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (!keystoreProperties.isEmpty) {
            create("release") {
                storeFile     = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias      = keystoreProperties.getProperty("keyAlias")
                keyPassword   = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true

            // R8 미적용
            optimization { enable = false }

            // 에뮬레이터 개발용
            ndk { abiFilters += setOf("arm64-v8a", "armeabi-v7a", "x86_64", "x86") }
        }

        release {
            isDebuggable = false

            // 실기기만 (대기 riscv64)
            ndk { abiFilters += setOf("arm64-v8a", "armeabi-v7a") }

            signingConfig = signingConfigs["release"]
//            signingConfigs.findByName("release")?.let { signingConfig = it }

            optimization {
                // R8 적용
                enable           = true

                // R8 적용 범위 제한 (해제함)
                // packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        checkReleaseBuilds = true                                                                   // release 빌드 시 lintVital 실행 — fatal 수준 문제만 빠르게 보는 간이 검사
        abortOnError       = true                                                                   // error 수준 이슈가 하나라도 있으면 빌드 실패
        warningsAsErrors   = false                                                                  // warning은 warning으로만 남김. 빌드를 막지 않음
        baseline           = file("lint-baseline.xml")                                              // 기존 이슈는 기록해 두고, 새로 생긴 것만 막음 (./gradlew :app:updateLintBaseline)
        printTextReport    = true                                                                   // 이슈 내용을 터미널(stdout)에 바로 출력 — build/reports 를 열지 않아도 확인 가능

//        disable += setOf("UnusedResources")                                                       // 이 검사 자체를 안 함 (끄기)
//        warning += setOf("...")                                                                   // warning 설정
//        error   += setOf("...")                                                                   // error 올림

        disable += setOf("ChromeOsAbiSupport")                                                      // 폰 전용 앱 — 크롬북(x86_64) 미지원 (release abiFilters 의도)

        // 보안 관련은 fatal 로 올림 — lint 실행과 release 빌드(lintVital) 모두에서 차단
        fatal += setOf(
            // 매니페스트
            "HardcodedDebugMode",                                                                   // 매니페스트에 debuggable 직접 지정 금지 (release 디버거 연결 방지)
            "AllowBackup",                                                                          // 백업 허용 금지 (adb 백업으로 앱 데이터 유출 방지)
            "ExportedService",                                                                      // 권한 없이 외부 노출된 Service
            "ExportedReceiver",                                                                     // 권한 없이 외부 노출된 BroadcastReceiver
            "ExportedContentProvider",                                                              // 권한 없이 외부 노출된 ContentProvider

            // 통신 (SSL/TLS)
            "TrustAllX509TrustManager",                                                             // 인증서 검증 안 하는 TrustManager (중간자 공격)
            "BadHostnameVerifier",                                                                  // 항상 true 반환하는 HostnameVerifier
            "SSLCertificateSocketFactoryGetInsecure",                                               // 검증 끈 SSL 소켓 (getInsecure) 사용
            "InsecureBaseConfiguration",                                                            // network_security_config 에서 평문 HTTP 전체 허용
            "AuthLeak",                                                                             // URL 에 아이디·비밀번호 포함 (user:pass@host)

            // 암호화
            "GetInstance",                                                                          // 취약한 Cipher 모드 (ECB·모드 미지정)
            "SecureRandom",                                                                         // SecureRandom 고정 seed (난수 예측 가능)

            // 파일 권한
            "WorldReadableFiles",                                                                   // MODE_WORLD_READABLE (다른 앱이 파일 읽기)
            "WorldWriteableFiles",                                                                  // MODE_WORLD_WRITEABLE (다른 앱이 파일 쓰기)
            "SetWorldReadable",                                                                     // File.setReadable(true, false) (모든 앱에 읽기 권한)

            // WebView
            "SetJavaScriptEnabled",                                                                 // WebView JavaScript 허용 (eKYC 화면만 @SuppressLint 로 예외)
            "JavascriptInterface",                                                                  // JS 브리지 메서드에 @JavascriptInterface 누락
            "WebViewClientOnReceivedSslError"                                                       // onReceivedSslError 에서 handler.proceed() (SSL 오류 무시)
        )
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

// ./gradlew --write-verification-metadata sha256 --refresh-dependencies help :app:assembleDebug :app:assembleRelease :app:lintRelease :app:lintDebug testDebugUnitTest :app:assembleDebugAndroidTest (= bundle exec fastlane verification_metadata)
dependencies {
    listOf(":di", ":domain", ":core").forEach { implementation(project(it)) }

    // App Dependencies
    implementation(libs.bundles.app.dependencies)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Test
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.bundles.app.test.dependencies)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.bundles.app.android.test.dependencies)
}