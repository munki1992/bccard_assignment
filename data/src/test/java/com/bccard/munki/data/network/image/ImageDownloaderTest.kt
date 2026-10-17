package com.bccard.munki.data.network.image

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Image Downloader Test
 *
 * @author 정문기
 */
class ImageDownloaderTest {

    // 내 컴퓨터 안에서 도는 가짜 이미지 서버 — 정해 둔 응답을 돌려준다
    private val server = MockWebServer()

    private val downloader = ImageDownloader(OkHttpClient())

    // 가짜 서버의 이미지 주소
    private val url get() = server.url("/photo-1.jpg").toString()

    @Before
    fun setUp() = server.start()

    @After
    fun tearDown() = server.shutdown()

    // Test ------------------------------------------------------------------------------------- //

    // assertNull   -> 값이 null 이면 통과, 아니면 실패
    // assertEquals -> (기대값, 실제값) 이 같으면 통과, 다르면 실패

    // Test ------------------------------------------------------------------------------------- //

    // 서버가 200 으로 이미지를 주면, 받은 내용이 그대로 block 에 전달되는가?
    @Test
    fun `download - 받기에 성공하면 받은 내용을 넘겨준다`() = runTest {
        // 준비 : 서버가 200 과 함께 "image-bytes" 를 주게 한다
        server.enqueue(MockResponse().setResponseCode(200).setBody("image-bytes"))

        // 실행 : 이미지를 받아서 글자로 읽는다
//        println(url)
        val result = downloader.download(url) { it.readBytes().decodeToString() }
//        println(result)

        // 확인 : 받은 내용이 "image-bytes" 이고, 요청한 주소가 /photo-1.jpg 인가?
        assertEquals("image-bytes", result)
        assertEquals("/photo-1.jpg", server.takeRequest().path)
    }

    // 서버가 404 를 주면 결과가 null 인가?
    @Test
    fun `download - 서버가 에러를 주면 null 을 준다`() = runTest {
        // 준비 : 서버가 404 를 주게 한다
        server.enqueue(MockResponse().setResponseCode(404))

        // 실행 : 이미지를 받는다
        val result = downloader.download(url) { it.readBytes() }

        // 확인 : 결과가 null 인가?
        assertNull(result)
    }

    // 서버에 연결할 수 없으면 결과가 null 인가?
    @Test
    fun `download - 서버에 연결할 수 없으면 null 을 준다`() = runTest {
        // 준비 : 주소를 받아 둔 뒤 서버를 끈다
        val url = url
        server.shutdown()

        // 실행 : 꺼진 서버에서 이미지를 받는다
        val result = downloader.download(url) { it.readBytes() }

        // 확인 : 결과가 null 인가?
        assertNull(result)
    }

    // 받은 내용을 쓰다가(block) 에러가 나면 결과가 null 인가?
    @Test
    fun `download - 받은 내용을 쓰다가 에러가 나면 null 을 준다`() = runTest {
        // 준비 : 서버가 200 과 함께 "image-bytes" 를 주게 한다
        server.enqueue(MockResponse().setResponseCode(200).setBody("image-bytes"))

        // 실행 : 받은 내용을 쓰는 도중 에러가 난다 (예: 저장 공간 부족)
        val result = downloader.download(url) { error("쓰기 실패") }

        // 확인 : 결과가 null 인가?
        assertNull(result)
    }
}