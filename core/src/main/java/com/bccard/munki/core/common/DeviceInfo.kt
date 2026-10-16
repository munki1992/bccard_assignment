package com.bccard.munki.core.common

import android.os.Build

/**
 * Device Info
 *
 * @author 정문기
 */
object DeviceInfo {
    fun getDeviceModel()  = "${Build.MANUFACTURER} ${Build.MODEL} ${Build.DEVICE  ?: "Unknown"}"    // Android 디바이스 모델 정보
    fun getOSVersion()    = "Android ${Build.VERSION.RELEASE}"                                      // Android OS 버전 정보
    fun getSDKVersion()   = Build.VERSION.SDK_INT                                                   // Android SDK 버전 정보
}