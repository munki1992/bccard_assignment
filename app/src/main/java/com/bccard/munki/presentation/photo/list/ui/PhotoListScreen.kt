package com.bccard.munki.presentation.photo.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.bccard.munki.components.PhotoCard
import com.bccard.munki.components.RetryBox
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.foundation.base.BaseScreen
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.foundation.ui.toErrorMessage
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ClickFavorites
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ClickPhoto
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListIntent.ToggleLike
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListState
import com.bccard.munki.presentation.photo.list.ui.components.PhotoListTopBar

/**
 * Photo List Screen
 *
 * @author 정문기
 */
@Composable
fun PhotoListScreen(
    state: PhotoListState,
    photos: LazyPagingItems<Photo>,
    onIntent: (PhotoListIntent) -> Unit
) {
    val refresh = photos.loadState.refresh
    val append  = photos.loadState.append

    BaseScreen(
        topBar = { PhotoListTopBar(onFavoritesClick = { onIntent(ClickFavorites) }) }
    ) { _, innerPadding ->
        when (refresh) {

            // 첫 페이지 로딩
            is LoadState.Loading if photos.itemCount == 0 -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.colors.accent)
            }

            // 첫 페이지 실패
            is LoadState.Error if photos.itemCount == 0 -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                RetryBox(message = refresh.error.toErrorMessage(), onRetry = photos::retry)
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
                items(count = photos.itemCount, key = photos.itemKey { it.id.orEmpty() }) { index ->
                    photos[index]?.let { photo ->
                        PhotoCard(
                            photo = photo,
                            isLiked = photo.id in state.likedIds,
                            onClick = { photo.id?.let { onIntent(ClickPhoto(it)) } },
                            onLikeClick = { onIntent(ToggleLike(photo)) }
                        )
                    }
                }

                // 다음 페이지 로딩·실패
                if (append is LoadState.Loading || append is LoadState.Error) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (append is LoadState.Error)
                                RetryBox(message = append.error.toErrorMessage(), onRetry = photos::retry)
                            else CircularProgressIndicator(color = AppTheme.colors.accent)
                        }
                    }
                }
            }
        }
    }
}