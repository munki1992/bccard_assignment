package com.bccard.munki.foundation.navigation.app

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bccard.munki.core.consts.Constants

/**
 * App NavHost
 *
 * @author 정문기
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: AppRoute = AppRoute.PhotoList,
    navGraphBuilder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideIntoContainer(SlideDirection.Start, tween(Constants.TRANSITION_DURATION)) },
        exitTransition = { slideOutOfContainer(SlideDirection.Start, tween(Constants.TRANSITION_DURATION)) },
        popEnterTransition = { slideIntoContainer(SlideDirection.End, tween(Constants.TRANSITION_DURATION)) },
        popExitTransition = { slideOutOfContainer(SlideDirection.End, tween(Constants.TRANSITION_DURATION)) },
        builder = navGraphBuilder
    )
}