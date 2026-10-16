package com.bccard.munki.data.local.like

import android.content.Context
import com.bccard.munki.data.network.image.ImageDownloader
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Liked Image Store
 *
 * 좋아요한 사진의 오프라인용 이미지 파일 보관함
 * - 받기는 ImageDownloader
 * - 여기서는 앱 내부 저장소(files/unsplash_liked)에 쓰고 지우기만 한다
 *
 * @author 정문기
 */
class LikedImageStore(
    private val context: Context,
    private val downloader: ImageDownloader
) {

    // 저장 폴더 — files/unsplash_liked (없으면 만든다)
    private val dir: File get() = File(context.filesDir, "unsplash_liked").apply { mkdirs() }

    // 이미지를 받아 files/unsplash_liked/{id}.jpg 로 저장 — 성공하면 파일 경로, 실패하면 null
    // 임시 파일(.tmp)에 다 받은 뒤 이름을 바꿔서, 받다 끊겨도 깨진 파일이 남지 않게 한다
    suspend fun save(id: String, url: String): String? = withContext(Dispatchers.IO) {
        val file = File(dir, "$id.jpg")
        val temp = File(dir, "$id.jpg.tmp")

        val downloaded = downloader.download(url) { input ->
            temp.outputStream().use { input.copyTo(it) }
        } != null

        if (downloaded && temp.renameTo(file)) {
            file.absolutePath
        } else {
            temp.delete()
            null
        }
    }

    // 저장해 둔 파일 삭제 — 경로가 없으면 아무 일도 안 한다
    suspend fun delete(path: String?) = withContext(Dispatchers.IO) {
        path?.let { File(it).delete() }
    }
}