package com.bccard.munki.presentation.sample

object SampleContract {

    data class SampleState(
        val text: String = "Sample"
    )

    sealed interface SampleIntent
}
