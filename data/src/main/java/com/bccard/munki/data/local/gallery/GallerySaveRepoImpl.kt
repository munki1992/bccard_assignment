package com.bccard.munki.data.local.gallery

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.bccard.munki.core.consts.Constants
import com.bccard.munki.data.network.image.ImageDownloader
import com.bccard.munki.domain.repository.local.GallerySaveRepo
import java.io.File
import java.io.InputStream

/**
 * Gallery Save Repo Impl
 *
 * - Android 10 이상은 MediaStore(권한 없음)
 * - 9 이하는 공용 Pictures 폴더에 파일로 저장(WRITE_EXTERNAL_STORAGE 필요)
 *
 * @author 정문기
 */
class GallerySaveRepoImpl(
    private val context: Context,
    private val downloader: ImageDownloader
) : GallerySaveRepo {

    // Override --------------------------------------------------------------------------------- //

    // 갤러리에 이미지 저장 — 성공 여부
    override suspend fun save(url: String, fileName: String): Boolean =
        downloader.download(url) { input ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                saveToMediaStore(input, fileName)
            else saveToPublicFolder(input, fileName)
        } ?: false


    // Function --------------------------------------------------------------------------------- //


    // Android 10 이상 — MediaStore 에 등록 (권한 없이 Pictures/UnsplashExplorer 에 저장)
    // IS_PENDING 동안은 갤러리에 안 보이고, 다 쓰면 0 으로 풀어 공개한다. 실패하면 만든 항목을 지운다
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToMediaStore(input: InputStream, fileName: String): Boolean {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, Constants.IMAGE_MIME_TYPE)
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${Constants.GALLERY_FOLDER}")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return false

        return runCatching {
            resolver.openOutputStream(uri)?.use { input.copyTo(it) } ?: error("output stream 없음")
            resolver.update(uri, ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }, null, null)
            true
        }.getOrElse {
            resolver.delete(uri, null, null)
            false
        }
    }

    // Android 9 이하 — 공용 Pictures/UnsplashExplorer 에 파일로 저장 후 갤러리에 알린다 (WRITE_EXTERNAL_STORAGE 필요)
    @Suppress("DEPRECATION")
    private fun saveToPublicFolder(input: InputStream, fileName: String): Boolean {
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.GALLERY_FOLDER).apply { mkdirs() }
        val file = File(dir, fileName)

        file.outputStream().use { input.copyTo(it) }
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf(Constants.IMAGE_MIME_TYPE), null)
        return true
    }
}