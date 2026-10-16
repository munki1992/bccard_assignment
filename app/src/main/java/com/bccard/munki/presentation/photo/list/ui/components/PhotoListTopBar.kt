package com.bccard.munki.presentation.photo.list.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bccard.munki.R
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.foundation.theme.TOP_BAR_GAP

/**
 * Photo List TopBar
 *
 * @author 정문기
 */
@Composable
fun PhotoListTopBar(
    onFavoritesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 4.dp, top = TOP_BAR_GAP),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.str_photo_list_title),
            style = MaterialTheme.typography.headlineMedium,
            color = AppTheme.colors.textStrong,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onFavoritesClick) {
            Icon(
                painter = painterResource(R.drawable.ic_favorite_border),
                contentDescription = stringResource(R.string.str_photo_list_favorites),
                tint = AppTheme.colors.error,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Preview -------------------------------------------------------------------------------------- //

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoListTopBarPreview() {
    AppTheme {
        PhotoListTopBar(onFavoritesClick = {})
    }
}