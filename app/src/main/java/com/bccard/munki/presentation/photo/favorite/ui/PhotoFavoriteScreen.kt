package com.bccard.munki.presentation.photo.favorite.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bccard.munki.R
import com.bccard.munki.foundation.base.BaseScreen
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.components.BackTopBar
import com.bccard.munki.components.PhotoCard
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ClickPhoto
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteIntent.ToggleLike
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteState

/**
 * Photo Favorite Screen
 *
 * @author 정문기
 */
@Composable
fun PhotoFavoriteScreen(
    state: PhotoFavoriteState,
    onBack: () -> Unit,
    onIntent: (PhotoFavoriteIntent) -> Unit
) {
    BaseScreen(
        topBar = { BackTopBar(title = stringResource(R.string.str_photo_favorite_title), onBack = onBack) }
    ) { _, innerPadding ->
        when {
            state.isLoading -> Unit

            // 빈 상태
            state.photos.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.msg_photo_favorite_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = state.photos, key = { it.id.orEmpty() }) { photo ->
                    PhotoCard(
                        photo = photo,
                        isLiked = true,
                        onClick = { photo.id?.let { onIntent(ClickPhoto(it)) } },
                        onLikeClick = { onIntent(ToggleLike(photo)) }
                    )
                }
            }
        }
    }
}