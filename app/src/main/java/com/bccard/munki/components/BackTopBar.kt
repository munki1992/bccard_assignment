package com.bccard.munki.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
 * Back TopBar
 *
 * 뒤로가기 + 제목 + 오른쪽 버튼(actions) 상단바 — 상세·좋아요 목록 공용
 *
 * @author 정문기
 */
@Composable
fun BackTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .padding(start = 4.dp, end = 4.dp, top = TOP_BAR_GAP),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.str_common_back),
                tint = AppTheme.colors.textStrong
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = AppTheme.colors.textStrong,
            modifier = Modifier.weight(1f)
        )

        actions()
    }
}

// Preview -------------------------------------------------------------------------------------- //

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BackTopBarPreview() {
    AppTheme {
        BackTopBar(title = "Photo Detail", onBack = {}) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_favorite),
                    contentDescription = null,
                    tint = AppTheme.colors.error
                )
            }
        }
    }
}