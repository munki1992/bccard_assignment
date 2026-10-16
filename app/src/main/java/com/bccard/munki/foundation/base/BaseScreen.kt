package com.bccard.munki.foundation.base

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.bccard.munki.foundation.ui.ScreenEnv
import com.bccard.munki.foundation.ui.rememberScreenEnv

/**
 * Compose Base Screen
 *
 * @author 정문기
 */
@Composable
fun BaseScreen(
    insets: WindowInsets = WindowInsets.safeDrawing,
    avoidKeyboard: Boolean = true,
    topBar: (@Composable (env: ScreenEnv) -> Unit)? = null,
    bottomBar: (@Composable (env: ScreenEnv) -> Unit)? = null,
    dismissKeyboardOnTap: Boolean = false,
    content: @Composable (env: ScreenEnv, innerPadding: PaddingValues) -> Unit
) {
    val env = rememberScreenEnv()
    val contentInsets = if (avoidKeyboard) insets else insets.exclude(WindowInsets.ime)

    Scaffold(
        contentWindowInsets = contentInsets,
        snackbarHost = { SnackbarHost(hostState = env.snackbarState) },
        topBar = { topBar?.invoke(env) },
        bottomBar = { bottomBar?.invoke(env) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .then(
                    if (dismissKeyboardOnTap) Modifier.pointerInput(Unit) {
                        detectTapGestures {
                            env.focus.clearFocus(true)
                            env.keyboard?.hide()
                        }
                    } else Modifier
                )
        ) {
            content(env, innerPadding)
        }
    }
}