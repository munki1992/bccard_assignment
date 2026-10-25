# Unsplash Photo Explorer

[![Unit Test](https://github.com/munki1992/bccard_assignment/actions/workflows/unit-test.yml/badge.svg)](https://github.com/munki1992/bccard_assignment/actions/workflows/unit-test.yml)

Unsplash API로 사진을 탐색하고, 마음에 드는 사진을 로컬에 저장(좋아요)하는 Android 앱입니다.

## 실행 방법

1. 프로젝트 루트의 `local.properties` 에 Unsplash Access Key를 추가합니다.

   ```
   UNSPLASH_ACCESS_KEY=3oL-jHie1qDPHYnV_XSKC8wn4-PI-1VuKUoYY1qew1M
   ```

2. Android Studio로 프로젝트를 열고 Gradle Sync 후 `app` 을 실행합니다.

- 키는 빌드 시 `BuildConfig.UNSPLASH_ACCESS_KEY` 로 들어갑니다. (`core/build.gradle.kts`)
- 요청 헤더: `Authorization: Client-ID {UNSPLASH_ACCESS_KEY}` (`AppRetrofitBuilder` 에서 추가)
- Base URL: `https://api.unsplash.com/` (`gradle.properties`)

## 개발 환경

| 항목 | 버전 |
|---|---|
| minSdk / targetSdk | 26 / 37 |
| Kotlin | 2.4.21 |
| AGP | 9.4.1 |
| Gradle | 9.8.2 |
| UI | Jetpack Compose (Material3) |
| Compose BOM | 2026.09.00 |

## 주요 기능

| 화면 | 기능 | 진입점 |
|---|---|---|
| 사진 목록 (Photo List) | 최신 사진 리스트, 무한 스크롤(Paging3), 좋아요 토글 | 앱 시작 |
| 사진 상세 (Photo Detail) | 고해상도 이미지, 작가 정보, 설명, 조회수·다운로드·위치·카메라·태그, 좋아요 토글, 갤러리 저장 | 목록에서 사진 클릭 |
| 좋아요 목록 (Favorite Photos) | 로컬 DB(Room)에 저장된 사진만 표시, 오프라인에서도 확인 가능 | 목록 상단 하트 아이콘 |

### 오프라인 지원

- 좋아요를 누르면 사진 정보를 Room(`liked_photo` 테이블)에 저장하고, 이미지 파일을 앱 내부 저장소(`files/unsplash_liked`)에 받아 둡니다.
- 좋아요 목록은 API 없이 Room만 구독하므로 오프라인에서도 보입니다.
- 상세 화면은 좋아요한 사진이면 저장된 정보로 먼저 그리고, API 응답이 오면 전체 정보로 교체합니다.
- 오프라인 상세는 저장한 기본 정보(이미지 · 설명 · 작가)만 표시하고, 조회수 · 다운로드 수 · 태그 등은 온라인에서 표시합니다.

### 다운로드

- Unsplash API 정책에 따라 저장 전에 사진 정보의 `links.download_location`(없으면 `/photos/{id}/download`)을 호출합니다.
- Android 10 이상은 MediaStore로 권한 없이 `Pictures/UnsplashExplorer` 에 저장합니다.
- Android 9 이하는 `WRITE_EXTERNAL_STORAGE` 권한을 요청한 뒤 저장합니다.

## 사용 API

| 기능 | Endpoint |
|---|---|
| 사진 목록 | `GET /photos` |
| 사진 상세 | `GET /photos/{id}` |
| 다운로드 추적 | `GET /photos/{id}/download` |

## 아키텍처

Clean Architecture 기반 멀티 모듈 + Compose MVI 구조입니다.

```
app    → di, domain, core
di     → data, domain, core
data   → domain, core
domain → core

baselineprofile → app (측정 · 프로파일 생성 대상)
```

| 모듈 | 역할 |
|---|---|
| `app` | Compose 화면, ViewModel(MVI), 내비게이션, 테마, 공통 UI 컴포넌트 |
| `di` | Koin 모듈 (Retrofit/OkHttp, API 구현체, Room, Repository, UseCase) |
| `data` | Repository 구현체, Retrofit API, PagingSource, Room(DB·DAO·Entity), 이미지 다운로드·갤러리 저장 |
| `domain` | 모델(Photo), Repository 인터페이스, UseCase |
| `core` | 상수, 확장 함수, 네트워크 공통(ApiResult·ApiError·Retrofit Builder) |
| `baselineprofile` | Baseline Profile 생성 · 시작 성능 측정(Macrobenchmark). 실기기에서 `app` 을 자동 조작 |

### MVI

화면마다 `Contract`(State · Intent · Effect), `ViewModel`, `Route`, `Screen` 으로 나눕니다.

- **State**: `StateFlow` 로 화면 상태 전달
- **Intent**: 화면 → `ViewModel.onEvent()` 로 사용자 동작 전달
- **Effect**: `Channel` 로 일회성 이벤트 전달 (화면 이동, 토스트 등은 `BaseEffect` 로 공통 처리)
- **Route**: ViewModel 연결, Effect 처리, 권한 요청 등 플랫폼 처리
- **Screen**: State를 그리고 Intent만 보내는 순수 UI (Preview 제공)

## 사용 라이브러리

| 분류 | 라이브러리 |
|---|---|
| UI | Jetpack Compose, Material3, Navigation Compose, Coil3 |
| DI | Koin |
| Network | Retrofit, OkHttp, Moshi |
| Paging | Paging3 (paging-common, paging-compose) |
| Local DB | Room |
| 기타 | Coroutines/Flow, Timber, Kotlinx Serialization (내비게이션 경로) |
| Test | JUnit4, Coroutines Test, Turbine, MockK, MockWebServer, Robolectric, Compose UI Test |
| 성능 | Baseline Profile, Macrobenchmark, UiAutomator, ProfileInstaller |

## 성능 고려

- 목록 · 좋아요 목록에 사진 ID 를 `key` 로 지정해, 목록이 바뀔 때 같은 사진은 다시 그리지 않고 재사용합니다. 사진 목록은 Paging3 의 `itemKey` 를 사용해 아직 불러오지 않은 칸도 안전하게 처리합니다.
- 사진 목록은 `cachedIn(viewModelScope)` 로 캐시해 화면 회전 시 다시 요청하지 않습니다.
- 목록은 작은 이미지(`small`)를 사용합니다.
- 상세는 사진 색상 배경 → `small`(또는 저장 파일) → `full` 순서로 보여 주고, 비율로 자리를 미리 잡아 화면이 밀리지 않게 합니다.
- 네트워크 로그(`HttpLoggingInterceptor`)는 Debug 빌드에서만 추가합니다.
- 릴리스 빌드는 R8 로 앱 전체 코드 · 리소스를 최적화하고 난독화합니다. 크래시 분석용 줄 번호는 남기고 실제 파일 이름은 숨기며, `android.util.Log` 호출은 제거합니다. (`app/src/main/keepRules/rules.keep`)
- 릴리스 APK 는 실기기용 ABI(`arm64-v8a`, `armeabi-v7a`)만 포함하고, 디버그는 에뮬레이터용 `x86` · `x86_64` 까지 포함합니다.

### Baseline Profile

앱 시작 · 사진 목록 스크롤 · 좋아요 목록 이동 경로를 기록한 Baseline Profile 을 앱에 포함해, 설치 시 해당 코드를 미리 컴파일합니다.

| 항목 | 내용 |
|---|---|
| 생성 | `BaselineProfileGenerator` — 실기기에서 앱을 자동 조작해 실행되는 코드를 기록 → `app/src/release/generated/baselineProfiles/` |
| 측정 | `StartupBenchmarks` — 프로파일 없음 vs 적용, 콜드 스타트 시간 비교 (Macrobenchmark) |
| 사용자 동작 | `AppJourneys` — 앱 시작 → 목록 대기 → 스크롤 → 좋아요 목록 이동 · 뒤로가기 (생성 · 측정 공용) |
| 적용 | `profileinstaller` 로 설치 시 프로파일 컴파일 |

**측정 결과** (콜드 스타트 각 5회 중간값)

| 기기 | 상태 | 시작 시간 |
|---|---|---|
| Galaxy Z Fold7 (SM-F966N) · Android 17 | 프로파일 없음 | 193.5ms |
| Galaxy Z Fold7 (SM-F966N) · Android 17 | Baseline Profile 적용 | 154.1ms (**약 20% 개선**) |

- 프로파일은 R8 을 끈 `nonMinifiedRelease` 빌드에서 기록해야 실제 클래스 이름이 남고, release 빌드 때 R8 이 난독화 이름에 맞춰 변환합니다. AGP 9 의 `optimization {}` DSL 은 플러그인이 끄지 못해 `finalizeDsl` 에서 직접 끕니다. (`app/build.gradle.kts`)
- 측정은 `benchmarkRelease`(release 와 같은 R8 적용) 빌드로 실제 배포와 같은 조건에서 진행합니다.
- 생성 · 측정에는 Android 13 이상 실기기(또는 에뮬레이터)가 필요합니다.

## 보안

- 앱 데이터(좋아요 DB · 오프라인 이미지)는 클라우드 백업 · 기기 간 이전에서 모두 제외합니다. (`allowBackup="false"`, `data_extraction_rules.xml`, `backup_rules.xml`)
- 평문(HTTP) 통신을 금지하고, 기기에 기본 탑재된 인증서만 신뢰합니다. (`network_security_config.xml`)
- OkHttp(API · 이미지 다운로드)는 TLS 1.2 이상만 허용합니다. (`ConnectionSpec.MODERN_TLS`)
- 앱이 화면에 있는 동안 다른 앱의 오버레이 창을 숨겨 탭재킹을 막습니다. (Android 12 이상 `setHideOverlayWindows`, 미만은 가려진 상태의 터치 무시)
- 화면 캡처 차단(`FLAG_SECURE`) · 최근 앱 미리보기 가림(`setRecentsScreenshotEnabled`)은 `MainActivity` 에 주석 코드로 준비해 두었습니다. (공개 사진 앱이라 미적용)
- Lint 보안 검사(매니페스트 · SSL/TLS · 암호화 · 파일 권한 · WebView 등 19개)를 `fatal` 로 올려, 해당 문제가 생기면 릴리스 빌드가 실패합니다.
- 종속 항목 확인(Dependency Verification)으로 라이브러리의 SHA-256 지문을 검증해, 바꿔치기된 라이브러리가 빌드에 들어오지 않게 합니다. (`gradle/verification-metadata.xml`)
- 릴리스 빌드는 R8 로 난독화하고, 네트워크 로그는 Debug 빌드에서만 남깁니다.
- API Key 는 소스코드에 두지 않고 `local.properties` → `BuildConfig` 로 주입합니다.
- `keystore/` 의 서명키는 평가자가 릴리스를 바로 빌드할 수 있도록 포함한 테스트용입니다. (실서비스에서는 CI Secret 으로 관리)
- 실서비스라면: API Key 는 앱에 두지 않고 서버를 거쳐 호출하고, SSL Pinning · 루팅/위변조 탐지 · 민감 화면 캡처 방지(`FLAG_SECURE`)를 함께 적용합니다.

## CI · 자동화

| 도구 | 내용 |
|---|---|
| GitHub Actions | `main` push · PR 마다 단위 테스트 · UI 테스트 실행 (`.github/workflows/unit-test.yml`) |
| CodeQL | push 마다 Kotlin · Actions · Ruby 코드 보안 분석 (GitHub 기본 설정) |
| Dependabot | Gradle 라이브러리는 보안 취약점이 생기면, GitHub Actions 는 새 버전이 나오면 PR 자동 생성 (`.github/dependabot.yml`) |
| fastlane | 반복 작업을 명령 하나로 실행 (`fastlane/Fastfile`) |

```
// Lint 검사 — 코드를 바꿀 때마다 (baseline 에 없는 새 이슈가 있으면 실패)
bundle exec fastlane lint

// Lint baseline 정리 — baseline 에 있던 이슈를 고쳤을 때
bundle exec fastlane lint_baseline

// 종속 항목 확인 지문 갱신 — 라이브러리를 추가하거나 버전을 올렸을 때 (Sync 전에 실행)
bundle exec fastlane verification_metadata

// Baseline Profile 생성 — 화면 · 주요 흐름이 바뀌었을 때 (실기기 필요)
bundle exec fastlane baseline_profile

// 시작 시간 비교 — 프로파일 없음 vs 적용, 개선율 출력 (baseline_profile 다음, 실기기 필요)
bundle exec fastlane startup_benchmark
```

- `verification_metadata` 는 지문 생성과 함께 Android Studio Sync 용 소스 · javadoc 예외 규칙, OS 별(macOS · Linux · Windows) `aapt2` 지문까지 자동으로 채웁니다.
- Dependabot 의 Gradle 보안 업데이트 PR 은 해당 브랜치에서 `verification_metadata` 를 실행해 지문을 갱신한 뒤 merge 합니다.

## 트러블슈팅

**목록에 같은 사진이 다시 나오는 문제**

- 원인: `GET /photos` 는 최신순 + 페이지 번호 방식이라, 스크롤하는 사이 새 사진이 올라오면 다음 페이지에 이미 받은 사진이 다시 포함됩니다. 같은 ID 가 `LazyVerticalGrid` key 로 중복되면 크래시가 날 수 있습니다.
- 해결: `PhotoPagingSource` 에서 받은 사진 ID 를 기억하고, 이미 받은 ID 는 결과에서 제외합니다. (`PhotoPagingSourceTest` 로 확인)

## 테스트

```
// 테스트
./gradlew testDebugUnitTest

// 테스트 (초기화)
./gradlew testDebugUnitTest --rerun-tasks
```

로컬 테스트(JVM)로 기기 없이 실행합니다. Compose UI 테스트도 Robolectric 으로 함께 실행되어 CI(GitHub Actions)에서 자동으로 확인합니다.

| 모듈 | 대상 | 내용 |
|---|---|---|
| `data` | `LikedPhotoMapper` | 사진 ↔ 엔티티 변환 (ID 없으면 저장 안 함, 저장하지 않는 값은 비워 둠) |
| `data` | `PhotoPagingSource` | 1페이지부터 요청, 다음 페이지 번호, 빈 페이지면 끝, 중복 사진 제거, API 에러 |
| `data` | `LikeRepoImpl` | 좋아요 추가 · 취소, 이미지 파일 경로 기록, 이미지 받는 중 좋아요 취소 시 파일 삭제 |
| `data` | `PhotoRepoImpl` | 인터넷 없음 처리, 401 재요청, 서버 에러, 다운로드 추적 주소 선택 (`download_location` / `photos/{id}/download`) |
| `data` | `ImageDownloader` | 가짜 서버(MockWebServer)로 받기 성공 · 서버 에러 · 연결 실패 · 쓰기 실패 |
| `app` | `PhotoListViewModel` | 좋아요 상태 반영, 하트 토글, 상세 · 좋아요 목록 화면 이동 |
| `app` | `PhotoFavoriteViewModel` | 좋아요 목록 표시, 하트 끄면 목록에서 제거, 상세 화면 이동 |
| `app` | `PhotoDetailViewModel` | 상세 표시, **오프라인일 때 저장해 둔 정보 표시**, 오류 토스트, 다운로드 성공 · 실패, 권한 거부 |
| `app` | `RetryBox` (UI) | 오류 문구 표시, 다시 시도 버튼 클릭 |
| `app` | `PhotoFavoriteScreen` (UI) | 빈 상태 문구, 로딩 중 빈 상태 숨김, 사진 표시, 사진 · 하트 · 뒤로가기 클릭 |

- 테스트는 `준비 / 실행 / 확인` 순서로 주석을 달았습니다.
- `PhotoDetailViewModel` 은 `SavedStateHandle.toRoute()` 가 안드로이드 `Bundle` 을 사용해 Robolectric 으로 실행합니다.
- UI 테스트는 ViewModel 없이 State 를 직접 넣어 화면을 그리고, 화면에서 나가는 Intent 를 확인합니다.
- 갤러리 저장(`GallerySaveRepoImpl`)은 MediaStore 를 사용해 단위 테스트에서 제외했습니다.