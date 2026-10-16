package com.bccard.munki.foundation.navigation.app

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bccard.munki.foundation.navigation.app.AppRoute.PhotoDetail
import com.bccard.munki.foundation.navigation.app.AppRoute.PhotoList
import com.bccard.munki.presentation.photo.detail.PhotoDetailScreenRoute
import com.bccard.munki.presentation.photo.list.PhotoListScreenRoute

/**
 * App NavGraph
 *
 * @author 정문기
 */
fun NavGraphBuilder.appNavGraph(appNavigator: AppNavigator) {

    composable<PhotoList> { PhotoListScreenRoute(appNavigator) }
    composable<PhotoDetail> { PhotoDetailScreenRoute(appNavigator) }
}