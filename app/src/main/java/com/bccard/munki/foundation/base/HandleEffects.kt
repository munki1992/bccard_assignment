package com.bccard.munki.foundation.base

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.bccard.munki.foundation.base.BaseEffect.Navigate
import com.bccard.munki.foundation.base.BaseEffect.NavigateBack
import com.bccard.munki.foundation.base.BaseEffect.NavigateWithData
import com.bccard.munki.foundation.base.BaseEffect.ShowToast
import com.bccard.munki.foundation.base.BaseEffect.ShowToastMessage
import com.bccard.munki.foundation.navigation.app.AppNavigator
import com.bccard.munki.foundation.ui.ScreenEnv
import kotlinx.coroutines.launch

/**
 * Handle Effects
 *
 * @author 정문기
 */
@Composable
fun <EFFECT> HandleEffects(
    viewModel: BaseViewModel<*, EFFECT, *>,
    appNavigator: AppNavigator,
    env: ScreenEnv,
    onEffect: (suspend AppNavigator.(EFFECT) -> Unit)? = null
) {
    LaunchedEffect(env.lifecycleOwner) {
        env.lifecycleOwner.repeatOnLifecycle(STARTED) {
            launch {
                viewModel.baseEffect.collect { eff ->
                    when (eff) {
                        NavigateBack        -> appNavigator.pop()
                        is Navigate         -> appNavigator.push(eff.route)
                        is NavigateWithData -> appNavigator.pushWithData(eff.route, eff.key, eff.data)
                        is ShowToast        -> Toast.makeText(env.context, env.resources.getString(eff.messageRes, *eff.args.toTypedArray()), Toast.LENGTH_SHORT).show()
                        is ShowToastMessage -> Toast.makeText(env.context, eff.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // 화면 고유
            onEffect?.let { handler ->
                launch { viewModel.effect.collect { eff -> appNavigator.handler(eff) } }
            }
        }
    }
}