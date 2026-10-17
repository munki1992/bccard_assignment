package com.bccard.munki.presentation.photo.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.bccard.munki.MainDispatcherRule
import com.bccard.munki.R
import com.bccard.munki.core.networks.ApiError
import com.bccard.munki.core.networks.ApiResult
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoDownloadDTO
import com.bccard.munki.domain.usecase.local.GallerySaveUseCase
import com.bccard.munki.domain.usecase.local.LikeUseCase
import com.bccard.munki.domain.usecase.network.photo.PhotoUseCase
import com.bccard.munki.fake.FakeGallerySaveRepo
import com.bccard.munki.fake.FakeLikeRepo
import com.bccard.munki.fake.FakePhotoRepo
import com.bccard.munki.foundation.base.BaseEffect.ShowToast
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.Download
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.DownloadPermissionDenied
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Photo Detail ViewModel Test
 *
 * @author 정문기
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PhotoDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // API 에서 받은 사진 (조회수까지 있는 전체 정보)
    private val apiPhoto = Photo(id = "photo-1", description = "API 설명", views = 100)

    // 좋아요할 때 저장해 둔 사진 (오프라인용 이미지 경로 포함)
    private val likedPhoto = Photo(id = "photo-1", description = "저장한 설명", localImagePath = "/files/unsplash_liked/photo-1.jpg")

    // 목록에서 photo-1 을 눌러 들어온 상세 화면 만들기
    private fun viewModel(
        photoRepo: FakePhotoRepo = FakePhotoRepo(detailResult = ApiResult.Success(apiPhoto)),
        likeRepo: FakeLikeRepo = FakeLikeRepo(),
        galleryRepo: FakeGallerySaveRepo = FakeGallerySaveRepo()
    ) = PhotoDetailViewModel(
        savedStateHandle   = SavedStateHandle(mapOf("photoId" to "photo-1")),
        photoUseCase       = PhotoUseCase(photoRepo),
        likeUseCase        = LikeUseCase(likeRepo),
        gallerySaveUseCase = GallerySaveUseCase(galleryRepo)
    )

    // Test ------------------------------------------------------------------------------------- //

    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // assertTrue   -> 조건이 true 면 통과, false 면 실패
    // assertFalse  -> 조건이 false 면 통과, true 면 실패
    // test { }     -> Flow 를 구독해서, awaitItem() 으로 다음에 나온 값을 꺼낸다 (Turbine)

    // Test ------------------------------------------------------------------------------------- //

    // API 가 성공하면 State 의 사진이 API 사진이고, 로딩이 끝났는가?
    @Test
    fun `loadDetail - API 가 성공하면 받은 사진을 보여 준다`() = runTest {
        // 실행 : 상세 화면을 연다 (API 성공)
        val viewModel = viewModel()

        // 확인 : State 의 사진이 API 사진이고, 로딩이 끝났는가?
        assertEquals(apiPhoto, viewModel.state.value.photo)
        assertFalse(viewModel.state.value.isLoading)
    }

    // 좋아요한 사진이면 API 가 실패해도 State 의 사진이 저장해 둔 사진인가?
    @Test
    fun `loadDetail - 오프라인이어도 좋아요한 사진이면 저장해 둔 정보를 보여 준다`() = runTest {
        // 준비 : photo-1 이 좋아요된 상태 + 인터넷 없음
        val photoRepo = FakePhotoRepo(detailResult = ApiResult.Failure(ApiError.NoInternet))
        val likeRepo = FakeLikeRepo(liked = listOf(likedPhoto))

        // 실행 : 상세 화면을 연다
        val viewModel = viewModel(photoRepo = photoRepo, likeRepo = likeRepo)

        // 확인 : State 의 사진이 저장해 둔 사진이고, 로딩이 끝났는가?
        assertEquals(likedPhoto, viewModel.state.value.photo)
        assertFalse(viewModel.state.value.isLoading)
    }

    // 좋아요한 사진이고 API 도 성공하면, API 사진에 저장해 둔 이미지 경로가 붙어 있는가?
    @Test
    fun `loadDetail - 좋아요한 사진이면 API 정보로 바꿔도 이미지 경로는 남긴다`() = runTest {
        // 준비 : photo-1 이 좋아요된 상태 (API 는 성공)
        val likeRepo = FakeLikeRepo(liked = listOf(likedPhoto))

        // 실행 : 상세 화면을 연다
        val viewModel = viewModel(likeRepo = likeRepo)

        // 확인 : State 의 사진이 API 사진 + 저장해 둔 이미지 경로인가?
        assertEquals(apiPhoto.copy(localImagePath = likedPhoto.localImagePath), viewModel.state.value.photo)
    }

    // 좋아요하지 않은 사진인데 인터넷이 없으면, 인터넷 오류 토스트가 나오는가?
    @Test
    fun `loadDetail - 보여 줄 사진이 없는데 API 가 실패하면 오류 토스트를 띄운다`() = runTest {
        // 준비 : 좋아요 안 한 사진 + 인터넷 없음
        val photoRepo = FakePhotoRepo(detailResult = ApiResult.Failure(ApiError.NoInternet))

        // 실행 : 상세 화면을 연다
        val viewModel = viewModel(photoRepo = photoRepo)

        // 확인 : 인터넷 오류 토스트 이벤트가 나왔는가?
        viewModel.baseEffect.test {
            assertEquals(ShowToast(R.string.msg_common_network_error), awaitItem())
        }
    }

    // 좋아요한 사진이면 State 의 하트가 켜져 있는가?
    @Test
    fun `isLiked - 좋아요한 사진이면 하트를 켠다`() = runTest {
        // 실행 : 좋아요한 photo-1 의 상세 화면을 연다
        val viewModel = viewModel(likeRepo = FakeLikeRepo(liked = listOf(likedPhoto)))

        // 확인 : State 의 하트가 켜져 있는가?
        assertTrue(viewModel.state.value.isLiked)
    }

    // 다운로드하면 받은 원본 주소와 파일 이름으로 갤러리 저장을 부르고, 완료 토스트가 나오는가?
    @Test
    fun `Download - 다운로드에 성공하면 갤러리에 저장하고 완료 토스트를 띄운다`() = runTest {
        // 준비 : 다운로드 API 가 원본 이미지 주소를 주는 상태
        val photoRepo = FakePhotoRepo(
            detailResult   = ApiResult.Success(apiPhoto),
            downloadResult = ApiResult.Success(PhotoDownloadDTO.Response(url = "original-url"))
        )
        val galleryRepo = FakeGallerySaveRepo(result = true)
        val viewModel = viewModel(photoRepo = photoRepo, galleryRepo = galleryRepo)

        viewModel.baseEffect.test {
            // 실행 : 다운로드 버튼을 누른다
            viewModel.onEvent(Download)

            // 확인 : 완료 토스트가 나왔고, 원본 주소 · 파일 이름으로 저장을 불렀고, 다운로드 중 표시가 꺼졌는가?
            assertEquals(ShowToast(R.string.msg_photo_detail_download_done), awaitItem())
            assertEquals(listOf("original-url" to "unsplash_photo-1.jpg"), galleryRepo.saved)
            assertFalse(viewModel.state.value.isDownloading)
        }
    }

    // 갤러리 저장이 실패하면 실패 토스트가 나오는가?
    @Test
    fun `Download - 갤러리 저장에 실패하면 실패 토스트를 띄운다`() = runTest {
        // 준비 : 다운로드 API 는 성공, 갤러리 저장은 실패하는 상태
        val photoRepo = FakePhotoRepo(
            detailResult   = ApiResult.Success(apiPhoto),
            downloadResult = ApiResult.Success(PhotoDownloadDTO.Response(url = "original-url"))
        )
        val viewModel = viewModel(photoRepo = photoRepo, galleryRepo = FakeGallerySaveRepo(result = false))

        viewModel.baseEffect.test {
            // 실행 : 다운로드 버튼을 누른다
            viewModel.onEvent(Download)

            // 확인 : 실패 토스트가 나왔는가?
            assertEquals(ShowToast(R.string.msg_photo_detail_download_fail), awaitItem())
        }
    }

    // 저장 권한을 거부하면 권한 안내 토스트가 나오는가?
    @Test
    fun `DownloadPermissionDenied - 저장 권한을 거부하면 안내 토스트를 띄운다`() = runTest {
        // 준비 : 상세 화면
        val viewModel = viewModel()

        viewModel.baseEffect.test {
            // 실행 : 저장 권한 거부 (Android 9 이하)
            viewModel.onEvent(DownloadPermissionDenied)

            // 확인 : 권한 안내 토스트가 나왔는가?
            assertEquals(ShowToast(R.string.msg_photo_detail_download_permission), awaitItem())
        }
    }
}