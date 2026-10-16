package com.bccard.munki.presentation.photo.favorite.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.photo.favorite.PhotoFavoriteContract.PhotoFavoriteState

/**
 * Photo Favorite Preview
 *
 * @author 정문기
 */
@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoFavoriteScreenPreview() {
    AppTheme {
        PhotoFavoriteScreen(
            state = PhotoFavoriteState(
                photos = List(4) { Photo(id = "$it", color = "#3C3C47", user = PhotoUser(name = "Alex Smith")) },
                isLoading = false
            ),
            onBack = {},
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoFavoriteEmptyPreview() {
    AppTheme {
        PhotoFavoriteScreen(
            state = PhotoFavoriteState(isLoading = false),
            onBack = {},
            onIntent = {}
        )
    }
}