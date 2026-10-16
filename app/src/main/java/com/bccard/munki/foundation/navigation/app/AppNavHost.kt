package com.bccard.munki.foundation.navigation.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

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
        builder = navGraphBuilder
    )
}