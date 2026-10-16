package com.bccard.munki.presentation.photo.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bccard.munki.foundation.base.HandleEffects
import com.bccard.munki.foundation.navigation.app.AppNavigator
import com.bccard.munki.foundation.platform.permission.StoragePermission
import com.bccard.munki.foundation.ui.rememberScreenEnv
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.Download
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.DownloadPermissionDenied
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

    // 저장 권한 (Android 9 이하만) — 허용되면 다운로드, 거부되면 안내
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        mViewModel.onEvent(if (granted) Download else DownloadPermissionDenied)
    }

    // UI
    PhotoDetailScreen(
        state = state,
        onBack = mViewModel::onBack,
        onDownloadClick = {
            if (StoragePermission.isGranted(env.context))
                mViewModel.onEvent(Download)
            else permissionLauncher.launch(StoragePermission.PERMISSION)
        },
        onIntent = mViewModel::onEvent
    )
}