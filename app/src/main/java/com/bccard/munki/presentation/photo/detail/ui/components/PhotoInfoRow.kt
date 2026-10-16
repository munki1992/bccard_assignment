package com.bccard.munki.presentation.photo.detail.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bccard.munki.R
import com.bccard.munki.foundation.theme.AppTheme

/**
 * Photo Info Row
 *
 * @author 정문기
 */
@Composable
fun PhotoInfoRow(@StringRes labelRes: Int, value: String) {
    Row {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            modifier = Modifier.width(72.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.textBody
        )
    }
}

// Preview -------------------------------------------------------------------------------------- //

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PhotoInfoRowPreview() {
    AppTheme {
        PhotoInfoRow(labelRes = R.string.str_photo_detail_location, value = "Reykjavík, Iceland")
    }
}