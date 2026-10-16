package com.bccard.munki.presentation.photo.detail.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bccard.munki.domain.model.network.photo.Photo
import com.bccard.munki.domain.model.network.photo.PhotoExif
import com.bccard.munki.domain.model.network.photo.PhotoLocation
import com.bccard.munki.domain.model.network.photo.PhotoTag
import com.bccard.munki.domain.model.network.photo.PhotoUser
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.photo.detail.PhotoDetailContract.PhotoDetailState

/**
 * Photo Detail Preview
 *
 * @author 정문기
 */
@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoDetailScreenPreview() {
    AppTheme {
        PhotoDetailScreen(
            state = PhotoDetailState(
                photo = Photo(
                    id = "preview",
                    width = 4000,
                    height = 4000,
                    color = "#3C3C47",
                    description = "설명",
                    user = PhotoUser(name = "정문기", username = "정문기"),
                    views = 1234567,
                    downloads = 1234,
                    location = PhotoLocation(name = "대한민국"),
                    exif = PhotoExif(name = "갤럭시"),
                    tags = listOf(PhotoTag(title = "테스트"))
                ),
                isLoading = false,
                isLiked = true
            ),
            onBack = {},
            onDownloadClick = {},
            onIntent = {}
        )
    }
}