package com.bccard.munki.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bccard.munki.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Retry Box Test
 *
 * @author 정문기
 */
@RunWith(RobolectricTestRunner::class)
class RetryBoxTest {

    // 테스트용 화면을 띄우는 규칙
    @get:Rule
    val composeRule = createComposeRule()

    // Test ------------------------------------------------------------------------------------- //

    // assertEquals      -> (기대값, 실제값) 이 같으면 통과, 다르면 실패
    // setContent { }    -> 테스트할 화면을 그린다
    // onNodeWithText()  -> 화면에서 그 글자를 가진 요소를 찾는다
    // assertIsDisplayed -> 찾은 요소가 화면에 보이면 통과, 아니면 실패
    // performClick()    -> 찾은 요소를 누른다

    // Test ------------------------------------------------------------------------------------- //

    // 넘겨준 오류 문구가 화면에 보이는가?
    @Test
    fun `RetryBox - 오류 문구를 보여 준다`() {
        // 준비 : 오류 문구를 넣어 RetryBox 를 그린다
        composeRule.setContent {
            AppTheme {
                RetryBox(message = "인터넷 연결을 확인해주세요.", onRetry = {})
            }
        }

        // 확인 : 오류 문구가 화면에 보이는가?
        composeRule.onNodeWithText("인터넷 연결을 확인해주세요.").assertIsDisplayed()
    }

    // 다시 시도 버튼을 누르면 onRetry 가 한 번 불리는가?
    @Test
    fun `RetryBox - 다시 시도를 누르면 onRetry 를 부른다`() {
        // 준비 : 눌린 횟수를 세는 onRetry 를 넣어 RetryBox 를 그린다
        var retryCount = 0
        composeRule.setContent {
            AppTheme {
                RetryBox(message = "인터넷 연결을 확인해주세요.", onRetry = { retryCount++ })
            }
        }

        // 실행 : 다시 시도 버튼을 누른다
        composeRule.onNodeWithText("다시 시도").performClick()

        // 확인 : onRetry 가 한 번 불렸는가?
        assertEquals(1, retryCount)
    }
}