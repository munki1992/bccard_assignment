package com.bccard.munki.presentation.photo.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bccard.munki.R
import com.bccard.munki.components.BackTopBar
import com.bccard.munki.foundation.base.BaseScreen
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailIntent.ToggleLike
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailState
import com.bccard.munki.presentation.photo.detail.ui.components.PhotoDetailContent

/**
 * Photo Detail Screen
 *
 * @author 정문기
 */
@Composable
fun PhotoDetailScreen(
    state: PhotoDetailState,
    onBack: () -> Unit,
    onDownloadClick: () -> Unit,
    onIntent: (PhotoDetailIntent) -> Unit
) {
    BaseScreen(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.str_photo_detail_title),
                onBack = onBack
            ) {
                // 다운로드 — 진행 중엔 로딩 표시 (사진을 불러오기 전엔 누를 수 없음)
                if (state.isDownloading) {
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppTheme.colors.accent, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                    }
                } else {
                    IconButton(onClick = onDownloadClick, enabled = state.photo != null) {
                        Icon(
                            painter = painterResource(R.drawable.ic_download),
                            contentDescription = stringResource(R.string.str_photo_detail_downloads),
                            tint = AppTheme.colors.textStrong
                        )
                    }
                }

                IconButton(onClick = { onIntent(ToggleLike) }) {
                    Icon(
                        painter = painterResource(if (state.isLiked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                        contentDescription = stringResource(R.string.str_photo_list_like),
                        tint = AppTheme.colors.error
                    )
                }
            }
        }
    ) { _, innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.photo != null -> PhotoDetailContent(photo = state.photo)

                state.isLoading     -> CircularProgressIndicator(
                    color = AppTheme.colors.accent,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}