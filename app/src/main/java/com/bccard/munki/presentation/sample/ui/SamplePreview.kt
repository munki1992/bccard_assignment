package com.bccard.munki.presentation.sample.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.sample.SampleContract.SampleState

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun SampleScreenPreview() {
    AppTheme {
        SampleScreen(
            state = SampleState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SampleScreenDarkPreview() {
    AppTheme {
        SampleScreen(
            state = SampleState(),
            onIntent = {}
        )
    }
}
