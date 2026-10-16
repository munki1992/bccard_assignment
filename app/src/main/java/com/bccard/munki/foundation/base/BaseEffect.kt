package com.bccard.munki.foundation.base

import androidx.annotation.StringRes
import com.bccard.munki.foundation.navigation.app.AppRoute

/**
 * Base Effect
 *
 * @author 정문기
 */
sealed interface BaseEffect {
    data object NavigateBack : BaseEffect

    data class Navigate(val route: AppRoute) : BaseEffect
    data class NavigateWithData(val route: AppRoute, val key: String, val data: Any?) : BaseEffect

    data class ShowToast(
        @param:StringRes val messageRes: Int,
        val args: List<Any> = emptyList()
    ) : BaseEffect

    data class ShowToastMessage(val message: String) : BaseEffect
}