package com.bccard.munki.foundation.navigation.app

import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

/**
 * App Route
 *
 * @author 정문기
 */
sealed class AppRoute {
    open val clearBackStack: Boolean get() = false
    open val navOptions: (NavOptionsBuilder.() -> Unit)? get() = { launchSingleTop = true }

    // 사진 ------------------------------------------------------------------------------------- //

    @Serializable
    data object PhotoList : AppRoute()

    @Serializable
    data class PhotoDetail(val photoId: String) : AppRoute()
}