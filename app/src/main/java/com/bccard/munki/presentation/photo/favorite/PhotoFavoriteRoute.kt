package com.bccard.munki.presentation.photo.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bccard.munki.foundation.base.HandleEffects
import com.bccard.munki.foundation.navigation.app.AppNavigator
import com.bccard.munki.foundation.ui.rememberScreenEnv
import com.bccard.munki.presentation.photo.favorite.ui.PhotoFavoriteScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Photo Favorite Route
 *
 * @author 정문기
 */
@Composable
fun PhotoFavoriteScreenRoute(appNavigator: AppNavigator) {
    val mViewModel = koinViewModel<PhotoFavoriteViewModel>()
    val state by mViewModel.state.collectAsStateWithLifecycle()
    val env = rememberScreenEnv()

    // Effect
    HandleEffects(mViewModel, appNavigator, env)

    // UI
    PhotoFavoriteScreen(
        state = state,
        onBack = mViewModel::onBack,
        onIntent = mViewModel::onEvent
    )
}