package com.bccard.munki.components.handler.back

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.bccard.munki.core.extensions.findActivityOrNull
import com.bccard.munki.core.extensions.showShortToast

/**
 * Back Double
 *
 * @author 정문기
 */
@Composable
fun BackDouble(
    exitMessage: String = "한 번 더 누르면 앱이 종료됩니다.",
    exitThreshold: Long = 2000L
) {
    val context = LocalContext.current
    val activity = context.findActivityOrNull()
    var backPressedTime by remember { mutableLongStateOf(0L) }

    // 제한 시간 안에 두 번 누르면 종료, 아니면 토스트
    BackHandler {
        val now = System.currentTimeMillis()
        if (now - backPressedTime <= exitThreshold) activity?.finish()
        else {
            backPressedTime = now
            context.showShortToast(exitMessage)
        }
    }
}