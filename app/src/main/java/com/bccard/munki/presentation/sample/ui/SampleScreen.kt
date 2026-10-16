package com.bccard.munki.presentation.sample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bccard.munki.foundation.theme.AppTheme
import com.bccard.munki.presentation.sample.SampleContract.SampleIntent
import com.bccard.munki.presentation.sample.SampleContract.SampleState

@Composable
fun SampleScreen(
    state: SampleState,
    onIntent: (SampleIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.text,
            style = MaterialTheme.typography.headlineMedium,
            color = AppTheme.colors.textPrimary
        )
    }
}
