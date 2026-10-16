package com.bccard.munki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.bccard.munki.foundation.navigation.app.AppNavHost
import com.bccard.munki.foundation.navigation.app.appNavGraph
import com.bccard.munki.foundation.navigation.app.rememberAppNavigator
import com.bccard.munki.foundation.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Main Compose Activity
 *
 * @author 정문기
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewModel

        setContent {
            val navController = rememberNavController()
            val appNavigator  = rememberAppNavigator(navController)

            AppTheme {
                AppNavHost(navController = navController) {
                    appNavGraph(appNavigator)
                }
            }
        }
    }
}