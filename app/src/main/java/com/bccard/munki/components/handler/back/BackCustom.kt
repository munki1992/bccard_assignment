package com.bccard.munki.components.handler.back

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/**
 * Back Custom
 *
 * @author 정문기
 */
@Composable
fun BackCustom(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
}