package com.bccard.munki.foundation.ui

import android.content.Context
import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope

/**
 * Remember Screen Env
 *
 * @author 정문기
 */
@Composable
fun rememberScreenEnv(): ScreenEnv {
    val scope          = rememberCoroutineScope()
    val snackBarState  = remember { SnackbarHostState() }
    val context        = LocalContext.current
    val resources      = LocalResources.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focus          = LocalFocusManager.current
    val keyboard       = LocalSoftwareKeyboardController.current

    return remember(scope, snackBarState, context, resources, lifecycleOwner, focus, keyboard) {
        ScreenEnv(
            scope = scope,
            snackbarState = snackBarState,
            context = context,
            resources = resources,
            lifecycleOwner = lifecycleOwner,
            focus = focus,
            keyboard = keyboard
        )
    }
}

@Stable
data class ScreenEnv(
    val scope: CoroutineScope,
    val snackbarState: SnackbarHostState,
    val context: Context,
    val resources: Resources,
    val lifecycleOwner: LifecycleOwner,
    val focus: FocusManager,
    val keyboard: SoftwareKeyboardController?
)