package com.bccard.munki.components.handler.back

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bccard.munki.core.extensions.findActivityOrNull

/**
 * Back One
 *
 * @author 정문기
 */
@Composable
fun BackOne() {
    val activity = LocalContext.current.findActivityOrNull()

    BackHandler { activity?.finish() }
}