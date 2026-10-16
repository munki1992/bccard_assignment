package com.bccard.munki.presentation.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bccard.munki.presentation.sample.ui.SampleScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SampleScreenRoute() {
    val mViewModel = koinViewModel<SampleViewModel>()
    val state by mViewModel.state.collectAsStateWithLifecycle()

    SampleScreen(
        state = state,
        onIntent = mViewModel::onEvent
    )
}
