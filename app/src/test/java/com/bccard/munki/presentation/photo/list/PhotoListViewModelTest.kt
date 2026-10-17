package com.bccard.munki.presentation.photo.list

import app.cash.turbine.test
import com.bccard.munki.MainDispatcherRule
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.usecase.local.LikeUseCase
import com.bccard.munki.domain.usecase.network.photo.PhotoUseCase
import com.bccard.munki.fake.FakeLikeRepo
import com.bccard.munki.fake.FakePhotoRepo
import com.bccard.munki.foundation.base.BaseEffect.Navigate
import com.bccard.munki.foundation.navigation.app.AppRoute
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ClickFavorites
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ClickPhoto
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ToggleLike
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Photo List ViewModel Test
 *
 * @author 정문기
 */
class PhotoListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // API 에서 받은 사진
    private val photo = Photo(id = "photo-1")

    // 좋아요 저장소(가짜)를 넣어 ViewModel 만들기
    private fun viewModel(likeRepo: FakeLikeRepo = FakeLikeRepo()) =
        PhotoListViewModel(PhotoUseCase(FakePhotoRepo()), LikeUseCase(likeRepo))

    // Test ------------------------------------------------------------------------------------- //

    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // test { }     -> Flow 를 구독해서, awaitItem() 으로 다음에 나온 값을 꺼낸다 (Turbine)

    // Test ------------------------------------------------------------------------------------- //

    // 이미 좋아요한 사진이 있으면 State 의 좋아요 ID 에 들어 있는가?
    @Test
    fun `likedIds - 좋아요한 사진 ID를 State 에 담는다`() = runTest {
        // 준비 : photo-1 이 이미 좋아요된 상태
        val likeRepo = FakeLikeRepo(liked = listOf(photo))

        // 실행 : 목록 화면을 연다 (ViewModel 생성)
        val viewModel = viewModel(likeRepo)

        // 확인 : State 의 좋아요 ID 가 photo-1 인가?
        assertEquals(setOf("photo-1"), viewModel.state.value.likedIds)
    }

    // 하트를 누르면 State 의 좋아요 ID 에 그 사진이 들어가는가?
    @Test
    fun `ToggleLike - 하트를 누르면 좋아요 ID 가 바뀐다`() = runTest {
        // 준비 : 좋아요가 하나도 없는 목록 화면
        val viewModel = viewModel()

        // 실행 : photo-1 의 하트를 누른다
        viewModel.onEvent(ToggleLike(photo))

        // 확인 : State 의 좋아요 ID 가 photo-1 인가?
        assertEquals(setOf("photo-1"), viewModel.state.value.likedIds)
    }

    // 사진을 누르면 그 사진 ID 로 상세 화면 이동 이벤트가 나오는가?
    @Test
    fun `ClickPhoto - 사진을 누르면 상세 화면으로 이동한다`() = runTest {
        // 준비 : 목록 화면
        val viewModel = viewModel()

        viewModel.baseEffect.test {
            // 실행 : photo-1 을 누른다
            viewModel.onEvent(ClickPhoto("photo-1"))

            // 확인 : photo-1 상세 화면으로 이동하는 이벤트가 나왔는가?
            assertEquals(Navigate(AppRoute.PhotoDetail("photo-1")), awaitItem())
        }
    }

    // 상단 하트를 누르면 좋아요 목록 화면 이동 이벤트가 나오는가?
    @Test
    fun `ClickFavorites - 상단 하트를 누르면 좋아요 목록 화면으로 이동한다`() = runTest {
        // 준비 : 목록 화면
        val viewModel = viewModel()

        viewModel.baseEffect.test {
            // 실행 : 상단 하트를 누른다
            viewModel.onEvent(ClickFavorites)

            // 확인 : 좋아요 목록 화면으로 이동하는 이벤트가 나왔는가?
            assertEquals(Navigate(AppRoute.PhotoFavorite), awaitItem())
        }
    }
}