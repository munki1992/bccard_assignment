package com.bccard.munki.foundation.navigation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bccard.munki.foundation.navigation.NavLogger

/**
 * remember App Navigator
 *
 * @author 정문기
 */
@Composable
fun rememberAppNavigator(nav: NavHostController): AppNavigator {
    DisposableEffect(nav) {
        val log      = NavLogger(NavLogger.HOST_APP)
        val listener = NavController.OnDestinationChangedListener { _, destination, args -> log.arrive(destination, args) }
            .also(nav::addOnDestinationChangedListener)

        onDispose { nav.removeOnDestinationChangedListener(listener) }
    }

    return remember(nav) { AppNavigatorImpl(nav) }
}