package com.bccard.munki.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bccard.munki.R
import com.bccard.munki.foundation.theme.AppTheme

/**
 * Retry Box
 *
 * 오류 문구 + 다시 시도 버튼
 *
 * @author 정문기
 */
@Composable
fun RetryBox(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        OutlinedButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.str_common_retry),
                color = AppTheme.colors.accent
            )
        }
    }
}

// Preview -------------------------------------------------------------------------------------- //

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RetryBoxPreview() {
    AppTheme {
        RetryBox(message = "인터넷 연결을 확인해주세요.", onRetry = {})
    }
}