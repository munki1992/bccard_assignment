package com.bccard.munki.presentation.photo.favorite

import app.cash.turbine.test
import com.bccard.munki.MainDispatcherRule
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.usecase.local.LikeUseCase
import com.bccard.munki.fake.FakeLikeRepo
import com.bccard.munki.foundation.base.BaseEffect.Navigate
import com.bccard.munki.foundation.navigation.app.AppRoute
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ClickPhoto
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ToggleLike
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

/**
 * Photo Favorite ViewModel Test
 *
 * @author 정문기
 */
class PhotoFavoriteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 좋아요한 사진 두 장
    private val photo1 = Photo(id = "photo-1")
    private val photo2 = Photo(id = "photo-2")

    // 좋아요 저장소(가짜)를 넣어 ViewModel 만들기
    private fun viewModel(likeRepo: FakeLikeRepo = FakeLikeRepo()) = PhotoFavoriteViewModel(LikeUseCase(likeRepo))

    // Test ------------------------------------------------------------------------------------- //

    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // assertFalse  -> 조건이 false 면 통과, true 면 실패
    // test { }     -> Flow 를 구독해서, awaitItem() 으로 다음에 나온 값을 꺼낸다 (Turbine)

    // Test ------------------------------------------------------------------------------------- //

    // 좋아요한 사진이 State 목록에 그대로 들어 있고, 로딩이 끝났는가?
    @Test
    fun `likedPhotos - 좋아요한 사진을 목록에 보여 준다`() = runTest {
        // 준비 : photo-1, photo-2 가 좋아요된 상태
        val likeRepo = FakeLikeRepo(liked = listOf(photo1, photo2))

        // 실행 : 좋아요 목록 화면을 연다 (ViewModel 생성)
        val viewModel = viewModel(likeRepo)

        // 확인 : 목록이 photo-1, photo-2 이고, 로딩이 끝났는가?
        assertEquals(listOf(photo1, photo2), viewModel.state.value.photos)
        assertFalse(viewModel.state.value.isLoading)
    }

    // 하트를 끄면 그 사진이 State 목록에서 빠지는가?
    @Test
    fun `ToggleLike - 하트를 끄면 목록에서 사라진다`() = runTest {
        // 준비 : photo-1, photo-2 가 좋아요된 목록 화면
        val viewModel = viewModel(FakeLikeRepo(liked = listOf(photo1, photo2)))

        // 실행 : photo-1 의 하트를 끈다
        viewModel.onEvent(ToggleLike(photo1))

        // 확인 : 목록에 photo-2 만 남았는가?
        assertEquals(listOf(photo2), viewModel.state.value.photos)
    }

    // 사진을 누르면 그 사진 ID 로 상세 화면 이동 이벤트가 나오는가?
    @Test
    fun `ClickPhoto - 사진을 누르면 상세 화면으로 이동한다`() = runTest {
        // 준비 : 좋아요 목록 화면
        val viewModel = viewModel(FakeLikeRepo(liked = listOf(photo1)))

        viewModel.baseEffect.test {
            // 실행 : photo-1 을 누른다
            viewModel.onEvent(ClickPhoto("photo-1"))

            // 확인 : photo-1 상세 화면으로 이동하는 이벤트가 나왔는가?
            assertEquals(Navigate(AppRoute.PhotoDetail("photo-1")), awaitItem())
        }
    }
}