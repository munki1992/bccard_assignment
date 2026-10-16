package com.bccard.munki.presentation.photo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bccard.munki.foundation.base.HandleEffects
import com.bccard.munki.foundation.navigation.app.AppNavigator
import com.bccard.munki.foundation.ui.rememberScreenEnv
import com.bccard.munki.presentation.photo.detail.ui.PhotoDetailScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Photo Detail Route
 *
 * @author 정문기
 */
@Composable
fun PhotoDetailScreenRoute(appNavigator: AppNavigator) {
    val mViewModel = koinViewModel<PhotoDetailViewModel>()
    val state by mViewModel.state.collectAsStateWithLifecycle()
    val env = rememberScreenEnv()

    // Effect
    HandleEffects(mViewModel, appNavigator, env)

    // UI
    PhotoDetailScreen(
        state = state,
        onBack = mViewModel::onBack,
        onDownloadClick = {},
        onIntent = mViewModel::onEvent
    )
}