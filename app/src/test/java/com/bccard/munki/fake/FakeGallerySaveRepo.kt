package com.bccard.munki.fake

import com.bccard.munki.domain.repository.local.GallerySaveRepo

/**
 * Fake Gallery Save Repo
 *
 * 갤러리에 저장하지 않고, 받은 값만 기록한 뒤 정해 둔 성공 여부를 돌려주는 가짜 GallerySaveRepo
 *
 * @author 정문기
 */
class FakeGallerySaveRepo(
    private val result: Boolean = true
) : GallerySaveRepo {

    // 저장 요청 기록 (이미지 주소, 파일 이름)
    val saved = mutableListOf<Pair<String, String>>()

    override suspend fun save(url: String, fileName: String): Boolean {
        saved += url to fileName
        return result
    }
}