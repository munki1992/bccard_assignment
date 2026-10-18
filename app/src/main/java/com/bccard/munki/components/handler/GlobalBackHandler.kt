package com.bccard.munki.components.handler

import androidx.compose.runtime.Composable
import com.bccard.munki.components.handler.back.BackCustom
import com.bccard.munki.components.handler.back.BackDisable
import com.bccard.munki.components.handler.back.BackDouble
import com.bccard.munki.components.handler.back.BackOne
import com.bccard.munki.core.foundation.state.BackState

/**
 * Global Back Handler
 *
 * @author 정문기
 */
@Composable
fun GlobalBackHandler(
    exitType: BackState,
    enabled: Boolean = true,
    exitMessage: String = "한 번 더 누르면 앱이 종료됩니다.",
    exitThreshold: Long = 2000L
) {
    if (!enabled) return

    when (exitType) {
        BackState.Disabled -> BackDisable()

        BackState.Double -> BackDouble(
            exitMessage,
            exitThreshold
        )

        BackState.Immediate -> BackOne()

        is BackState.Custom -> BackCustom(exitType.onBack)
    }
}