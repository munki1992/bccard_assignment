# Unsplash Photo Explorer

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
| Kotlin | 2.4.20 |
| AGP | 9.4.1 |
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
```

| 모듈 | 역할 |
|---|---|
| `app` | Compose 화면, ViewModel(MVI), 내비게이션, 테마, 공통 UI 컴포넌트 |
| `di` | Koin 모듈 (Retrofit/OkHttp, API 구현체, Room, Repository, UseCase) |
| `data` | Repository 구현체, Retrofit API, PagingSource, Room(DB·DAO·Entity), 이미지 다운로드·갤러리 저장 |
| `domain` | 모델(Photo), Repository 인터페이스, UseCase |
| `core` | 상수, 확장 함수, 네트워크 공통(ApiResult·ApiError·Retrofit Builder) |

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
| Test | JUnit4, Coroutines Test, Turbine, MockK, MockWebServer, Robolectric |

## 테스트

```
// 테스트
./gradlew testDebugUnitTest

// 테스트 (초기화)
./gradlew testDebugUnitTest --rerun-tasks
```

로컬 단위 테스트(JVM)로 기기 없이 실행합니다.

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

- 테스트는 `준비 / 실행 / 확인` 순서로 주석을 달았습니다.
- `PhotoDetailViewModel` 은 `SavedStateHandle.toRoute()` 가 안드로이드 `Bundle` 을 사용해 Robolectric 으로 실행합니다.
- 갤러리 저장(`GallerySaveRepoImpl`)은 MediaStore 를 사용해 단위 테스트에서 제외했습니다.