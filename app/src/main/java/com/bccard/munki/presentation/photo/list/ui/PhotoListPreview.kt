package com.bccard.munki.presentation.photo.list.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.photo.list.PhotoListContract.PhotoListState
import kotlinx.coroutines.flow.flowOf

/**
 * Photo List Preview
 *
 * @author 정문기
 */
@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoListScreenPreview() {
    val photos = List(6) { Photo(id = "$it", color = "#3C3C47", user = PhotoUser(name = "Alex Smith")) }

    AppTheme {
        PhotoListScreen(
            state = PhotoListState(likedIds = setOf("0")),
            photos = flowOf(PagingData.from(photos)).collectAsLazyPagingItems(),
            onIntent = {}
        )
    }
}