package com.bccard.munki.presentation.photo.favorite.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ClickPhoto
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ToggleLike
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Photo Favorite Screen Test
 *
 * @author 정문기
 */
@RunWith(RobolectricTestRunner::class)
class PhotoFavoriteScreenTest {

    // 테스트용 화면을 띄우는 규칙
    @get:Rule
    val composeRule = createComposeRule()

    // 좋아요한 사진 (사진 설명으로 화면에서 찾는다)
    private val photo = Photo(id = "photo-1", altDescription = "사진 설명")

    // 화면에서 보낸 Intent 기록
    private val intents = mutableListOf<PhotoFavoriteIntent>()

    // 뒤로가기가 눌린 횟수
    private var backCount = 0

    // State 를 넣어 좋아요 목록 화면 그리기 (ViewModel 없이)
    private fun setScreen(state: PhotoFavoriteState) = composeRule.setContent {
        AppTheme {
            PhotoFavoriteScreen(state = state, onBack = { backCount++ }, onIntent = { intents += it })
        }
    }

    // Test ------------------------------------------------------------------------------------- //

    // assertEquals                   -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // setContent { }                 -> 테스트할 화면을 그린다
    // onNodeWithText()               -> 화면에서 그 글자를 가진 요소를 찾는다 (substring = true 면 일부만 같아도 찾는다)
    // onNodeWithContentDescription() -> 화면에서 그 설명(contentDescription)을 가진 요소를 찾는다 (아이콘 · 이미지)
    // assertIsDisplayed              -> 찾은 요소가 화면에 보이면 통과, 아니면 실패
    // assertDoesNotExist             -> 찾은 요소가 화면에 없으면 통과, 있으면 실패
    // performClick()                 -> 찾은 요소를 누른다

    // Test ------------------------------------------------------------------------------------- //

    // 좋아요한 사진이 없으면 빈 상태 문구가 보이는가?
    @Test
    fun `PhotoFavoriteScreen - 좋아요한 사진이 없으면 빈 상태 문구를 보여 준다`() {
        // 준비 : 좋아요한 사진이 없는 화면
        setScreen(PhotoFavoriteState(photos = emptyList(), isLoading = false))

        // 확인 : 빈 상태 문구가 화면에 보이는가?
        composeRule.onNodeWithText("아직 좋아요한 사진이 없어요", substring = true).assertIsDisplayed()
    }

    // 목록을 받기 전(로딩 중)에는 빈 상태 문구가 없는가? (첫 화면에서 문구가 깜빡이지 않게)
    @Test
    fun `PhotoFavoriteScreen - 로딩 중에는 빈 상태 문구를 보여 주지 않는다`() {
        // 준비 : 아직 목록을 받기 전인 화면
        setScreen(PhotoFavoriteState(photos = emptyList(), isLoading = true))

        // 확인 : 빈 상태 문구가 화면에 없는가?
        composeRule.onNodeWithText("아직 좋아요한 사진이 없어요", substring = true).assertDoesNotExist()
    }

    // 좋아요한 사진이 있으면 그 사진이 보이는가?
    @Test
    fun `PhotoFavoriteScreen - 좋아요한 사진을 보여 준다`() {
        // 준비 : photo-1 이 좋아요된 화면
        setScreen(PhotoFavoriteState(photos = listOf(photo), isLoading = false))

        // 확인 : 사진(사진 설명)이 화면에 보이는가?
        composeRule.onNodeWithContentDescription("사진 설명").assertIsDisplayed()
    }

    // 사진을 누르면 그 사진 ID 로 ClickPhoto Intent 가 나가는가?
    @Test
    fun `PhotoFavoriteScreen - 사진을 누르면 ClickPhoto 를 보낸다`() {
        // 준비 : photo-1 이 좋아요된 화면
        setScreen(PhotoFavoriteState(photos = listOf(photo), isLoading = false))

        // 실행 : 사진을 누른다
        composeRule.onNodeWithContentDescription("사진 설명").performClick()

        // 확인 : ClickPhoto(photo-1) 이 전달됐는가?
        assertEquals(listOf(ClickPhoto("photo-1")), intents)
    }

    // 하트를 누르면 그 사진으로 ToggleLike Intent 가 나가는가?
    @Test
    fun `PhotoFavoriteScreen - 하트를 누르면 ToggleLike 를 보낸다`() {
        // 준비 : photo-1 이 좋아요된 화면
        setScreen(PhotoFavoriteState(photos = listOf(photo), isLoading = false))

        // 실행 : 하트(좋아요 버튼)를 누른다
        composeRule.onNodeWithContentDescription("좋아요").performClick()

        // 확인 : ToggleLike(photo-1) 이 전달됐는가?
        assertEquals(listOf(ToggleLike(photo)), intents)
    }

    // 뒤로가기를 누르면 onBack 이 한 번 불리는가?
    @Test
    fun `PhotoFavoriteScreen - 뒤로가기를 누르면 onBack 을 부른다`() {
        // 준비 : 좋아요 목록 화면
        setScreen(PhotoFavoriteState(photos = emptyList(), isLoading = false))

        // 실행 : 상단 뒤로가기 버튼을 누른다
        composeRule.onNodeWithContentDescription("뒤로가기").performClick()

        // 확인 : onBack 이 한 번 불렸는가?
        assertEquals(1, backCount)
    }
}