package com.bccard.munki.core.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false
}

// Context로부터 Activity 추출 (없으면 null 반환)
fun Context.findActivityOrNull(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.findActivityOrNull()
}

// 이전 Toast 를 취소하고 새로 띄운다 (연타 시 토스트 누적 방지)
private var currentToast: Toast? = null
fun Context.showShortToast(message: String): Toast {
    currentToast?.cancel()
    return Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        currentToast = this
        show()
    }
}