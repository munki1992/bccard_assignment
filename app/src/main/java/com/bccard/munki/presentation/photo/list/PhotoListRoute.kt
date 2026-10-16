package com.bccard.munki.presentation.photo.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.bccard.munki.foundation.base.HandleEffects
import com.bccard.munki.foundation.navigation.app.AppNavigator
import com.bccard.munki.foundation.ui.rememberScreenEnv
import com.bccard.munki.presentation.photo.list.ui.PhotoListScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Photo List Route
 *
 * @author 정문기
 */
@Composable
fun PhotoListScreenRoute(appNavigator: AppNavigator) {
    val mViewModel = koinViewModel<PhotoListViewModel>()
    val state by mViewModel.state.collectAsStateWithLifecycle()
    val photos = mViewModel.photos.collectAsLazyPagingItems()                                       // 예약 대기
    val env = rememberScreenEnv()

    // Effect
    HandleEffects(mViewModel, appNavigator, env)

    // UI
    PhotoListScreen(
        state = state,
        photos = photos,
        onIntent = mViewModel::onEvent
    )
}