package com.bccard.munki.data.network.image

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

/**
 * Image Downloader
 *
 * 이미지 주소(images.unsplash.com)에서 파일을 받아오는 일만 한다 — 어디에 저장할지는 쓰는 쪽이 정한다
 *
 * @author 정문기
 */
class ImageDownloader(
    private val client: OkHttpClient
) {
    suspend fun <T> download(url: String, block: (InputStream) -> T): T? = withContext(Dispatchers.IO) {
        runCatching {
            client.newCall(Request.Builder().url(url).build()).execute().use { response ->
                if (!response.isSuccessful)
                    null
                else response.body.byteStream().use(block)
            }
        }.getOrNull()
    }
}