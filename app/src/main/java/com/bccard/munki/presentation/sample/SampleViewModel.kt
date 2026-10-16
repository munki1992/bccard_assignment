package com.bccard.munki.presentation.sample

import androidx.lifecycle.ViewModel
import com.bccard.munki.presentation.sample.SampleContract.SampleIntent
import com.bccard.munki.presentation.sample.SampleContract.SampleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SampleViewModel : ViewModel() {

    private val _state = MutableStateFlow(SampleState())
    val state: StateFlow<SampleState> = _state.asStateFlow()

    fun onEvent(intent: SampleIntent) {
        when (intent) {
            else -> {}
        }
    }
}
