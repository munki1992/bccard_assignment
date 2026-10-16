package com.bccard.munki.foundation.platform.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Storage Permission
 *
 * 사진 저장 권한 — Android 9(API 28) 이하만 필요. 10 이상은 MediaStore 로 권한 없이 저장한다
 *
 * @author 정문기
 */
object StoragePermission {

    const val PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    // 이 기기에서 권한 요청이 필요한지 (Android 9 이하)
    val isRequired: Boolean get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    // 저장해도 되는지 — 10 이상은 항상 true
    fun isGranted(context: Context): Boolean =
        !isRequired || ContextCompat.checkSelfPermission(context, PERMISSION) == PackageManager.PERMISSION_GRANTED
}