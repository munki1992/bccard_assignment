package com.bccard.munki

import android.os.Build
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

        // if -> 다른 앱의 오버레이 창 숨김 — 투명 창으로 터치를 가로채는 탭재킹 방지 (Android 12 이상, HIDE_OVERLAY_WINDOWS 권한)
        // else -> Android 12 미만 — 오버레이를 숨길 수 없어서, 다른 창에 가려진 상태로 들어온 터치를 무시
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
//            window.setHideOverlayWindows(true)
//        else window.decorView.filterTouchesWhenObscured = true

        // 화면 캡처 · 녹화 · 화면 공유 차단 + 최근 앱 미리보기 가림 (민감 화면용, 모든 버전)
//        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)

        // 최근 앱 미리보기만 가림 — 스크린샷 · 녹화는 허용 (Android 13 이상)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
//            setRecentsScreenshotEnabled(false)

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